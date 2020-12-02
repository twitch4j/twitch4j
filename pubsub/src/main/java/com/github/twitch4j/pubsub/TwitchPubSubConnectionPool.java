package com.github.twitch4j.pubsub;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.twitch4j.common.pool.TwitchModuleConnectionPool;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.events.PubSubListenResponseEvent;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

/**
 * A pool for PubSub connections to help navigate rate-limits.
 * <p>
 * Warning: Passing a {@link java.util.concurrent.ScheduledThreadPoolExecutor} with too small corePoolSize can lead to
 * connections in the pool not behaving properly. Not specifying an executor allows connections to create their own
 * at will. If enough connections are made, this could pollute one's runtime environment.
 */
@SuperBuilder
public class TwitchPubSubConnectionPool extends TwitchModuleConnectionPool<TwitchPubSub, PubSubRequest, PubSubSubscription, Boolean, TwitchPubSubBuilder> implements ITwitchPubSub {

    private final String threadPrefix = "twitch4j-pool-" + RandomStringUtils.random(4, true, true) + "-pubsub-";

    private final Cache<String, PubSubSubscription> subscriptionsByNonce = Caffeine.newBuilder()
        .expireAfterWrite(30, TimeUnit.SECONDS)
        .build();

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
        if (topics > 1) throw new IllegalArgumentException("TwitchPubSubConnectionPool can only handle PubSubRequest's with a single topic subscription");
        final String nonce = injectNonce(pubSubRequest);
        final PubSubSubscription subscription = super.subscribe(pubSubRequest);
        subscriptionsByNonce.put(nonce, subscription);
        return subscription;
    }

    @Override
    protected TwitchPubSub createConnection() {
        // Instantiate with configuration
        TwitchPubSub client = advancedConfiguration.apply(
            TwitchPubSubBuilder.builder()
                .withEventManager(getConnectionEventManager())
                .withScheduledThreadPoolExecutor(getExecutor(threadPrefix + RandomStringUtils.random(4, true, true), TwitchPubSub.REQUIRED_THREAD_COUNT))
                .withProxyConfig(proxyConfig.get())
        ).build();

        // Reclaim topic headroom upon a failed subscription
        client.getEventManager().onEvent("twitch4j-pubsub-pool-nonce-tracker", PubSubListenResponseEvent.class, e -> {
            if (StringUtils.isNotEmpty(e.getNonce())) {
                PubSubSubscription subscription = subscriptionsByNonce.asMap().remove(e.getNonce());
                if (e.hasError() && subscription != null)
                    unsubscribe(subscription);
            }
        });

        // Return pubsub client
        return client;
    }

    @Override
    protected void disposeConnection(TwitchPubSub connection) {
        connection.close();
    }

    @Override
    protected PubSubSubscription handleSubscription(TwitchPubSub twitchPubSub, PubSubRequest pubSubRequest) {
        return twitchPubSub != null ? twitchPubSub.listenOnTopic(pubSubRequest) : null;
    }

    @Override
    protected PubSubSubscription handleDuplicateSubscription(TwitchPubSub twitchPubSub, PubSubRequest pubSubRequest) {
        return null;
    }

    @Override
    protected Boolean handleUnsubscription(TwitchPubSub twitchPubSub, PubSubSubscription pubSubSubscription) {
        return twitchPubSub != null ? twitchPubSub.unsubscribeFromTopic(pubSubSubscription) : null;
    }

    @Override
    protected PubSubRequest getRequestFromSubscription(PubSubSubscription subscription) {
        return subscription.getRequest();
    }

    private static String injectNonce(PubSubRequest req) {
        if (StringUtils.isBlank(req.getNonce()))
            req.setNonce(CryptoUtils.generateNonce(30));
        return req.getNonce();
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

}
