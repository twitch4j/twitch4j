package com.github.twitch4j.pubsub;

import com.github.twitch4j.common.pool.TwitchModuleConnectionPool;
import com.github.twitch4j.common.util.IncrementalReusableIdProvider;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.events.PubSubListenResponseEvent;
import com.github.twitch4j.util.IBackoffStrategy;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.stream.StreamSupport;

/**
 * A pool for PubSub connections to help navigate rate-limits.
 * <p>
 * Warning: Passing a {@link java.util.concurrent.ScheduledThreadPoolExecutor} with too small corePoolSize can lead to
 * connections in the pool not behaving properly. Not specifying an executor allows connections to create their own
 * at will. If enough connections are made, this could pollute one's runtime environment.
 */
@SuperBuilder
@Slf4j
public class TwitchPubSubConnectionPool extends TwitchModuleConnectionPool<TwitchPubSub, PubSubRequest, PubSubSubscription, Boolean, TwitchPubSubBuilder> implements ITwitchPubSub {
    private final IncrementalReusableIdProvider connectionIdProvider = new IncrementalReusableIdProvider();

    /**
     * The name of this connection
     * This name will be used in metrics / logging to identify this connection.
     */
    @NonNull
    @Builder.Default
    private String connectionName = RandomStringUtils.random(8, true, true);

    /**
     * WebSocket Connection Backoff Strategy
     */
    @Builder.Default
    private IBackoffStrategy connectionBackoffStrategy = null;

    @Override
    public PubSubSubscription listenOnTopic(PubSubRequest request) {
        return this.subscribe(request);
    }

    @Override
    public boolean unsubscribeFromTopic(PubSubSubscription subscription) {
        return this.unsubscribe(subscription);
    }

    @Override
    public PubSubSubscription subscribe(PubSubRequest pubSubRequest) {
        final int topics = getTopicCount(pubSubRequest);
        if (topics <= 0) return null;
        injectNonce(pubSubRequest);
        return super.subscribe(pubSubRequest);
    }

    @Override
    protected TwitchPubSub createConnection() {
        if (closed.get()) throw new IllegalStateException("PubSub connection cannot be created after pool was closed!");
        String connId = connectionIdProvider.get();

        // Instantiate with configuration
        TwitchPubSub client = advancedConfiguration.apply(
            TwitchPubSubBuilder.builder()
                .withConnectionName(connectionName)
                .withConnectionId(connId)
                .withEventManager(getConnectionEventManager())
                .withScheduledThreadPoolExecutor(getExecutor("twitch4j-pubsub-pool-" + connectionName + "-" + connId, TwitchPubSub.REQUIRED_THREAD_COUNT))
                .withProxyConfig(proxyConfig.get())
                .withConnectionBackoffStrategy(connectionBackoffStrategy)
        ).build();

        // Reclaim topic headroom upon a failed subscription
        client.getEventManager().onEvent("twitch4j-pubsub-pool-nonce-tracker", PubSubListenResponseEvent.class, e -> {
            if (e.hasError()) {
                e.getListenRequest().map(PubSubSubscription::new).ifPresent(this::unsubscribe);
            }
        });

        // Return pubsub client
        return client;
    }

    @Override
    protected void disposeConnection(TwitchPubSub connection) {
        connection.close();
        connectionIdProvider.release(connection.connectionId);
    }

    @Override
    protected PubSubSubscription handleSubscription(TwitchPubSub twitchPubSub, PubSubRequest pubSubRequest) {
        return twitchPubSub != null ? twitchPubSub.listenOnTopic(pubSubRequest) : null;
    }

    @Override
    protected PubSubSubscription handleDuplicateSubscription(TwitchPubSub twitchPubSub, TwitchPubSub old, PubSubRequest pubSubRequest) {
        final PubSubSubscription subscription = new PubSubSubscription(pubSubRequest);
        return twitchPubSub != null && twitchPubSub != old && twitchPubSub.unsubscribeFromTopic(subscription) ? subscription : null;
    }

    @Override
    protected Boolean handleUnsubscription(TwitchPubSub twitchPubSub, PubSubSubscription pubSubSubscription) {
        return twitchPubSub != null ? twitchPubSub.unsubscribeFromTopic(pubSubSubscription) : null;
    }

    @Override
    protected PubSubRequest getRequestFromSubscription(PubSubSubscription subscription) {
        return subscription.getRequest();
    }

    @Override
    protected int getSubscriptionSize(PubSubRequest pubSubRequest) {
        return getTopicCount(pubSubRequest);
    }

    private static void injectNonce(PubSubRequest req) {
        if (StringUtils.isBlank(req.getNonce()))
            req.setNonce(CryptoUtils.generateNonce(30));
    }

    private static int getTopicCount(PubSubRequest req) {
        Object topics = req.getData().get("topics");
        if (topics instanceof Collection)
            return ((Collection<?>) topics).size();
        else if (topics instanceof Iterable)
            return (int) StreamSupport.stream(((Iterable<?>) topics).spliterator(), false).count();
        else if (topics instanceof Object[])
            return ((Object[]) topics).length;
        return -1;
    }

    @Override
    public long getLatency() {
        long sum = 0;
        int count = 0;
        for (TwitchPubSub connection : getConnections()) {
            final long latency = connection.getLatency();
            if (latency > 0) {
                sum += latency;
                count++;
            }
        }
        return count > 0 ? sum / count : -1L;
    }
}
