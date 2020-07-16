package com.github.twitch4j.pubsub;

import com.github.twitch4j.common.pool.TwitchModuleConnectionPool;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import lombok.experimental.SuperBuilder;

/**
 * A pool for PubSub connections to help navigate rate-limits.
 * <p>
 * Warning: Passing a {@link java.util.concurrent.ScheduledThreadPoolExecutor} with too small corePoolSize can lead to
 * connections in the pool not behaving properly. Not specifying an executor allows connections to create their own
 * at will. If enough connections are made, this could pollute one's runtime environment.
 */
@SuperBuilder
public class TwitchPubSubConnectionPool extends TwitchModuleConnectionPool<TwitchPubSub, PubSubRequest, PubSubSubscription, Class<Void>> {

    @Override
    protected TwitchPubSub createConnection() {
        return TwitchPubSubBuilder.builder()
            .withEventManager(getConnectionEventManager())
            .withScheduledThreadPoolExecutor(executor.get())
            .withProxyConfig(proxyConfig.get())
            .build();
    }

    @Override
    protected void disposeConnection(TwitchPubSub connection) {
        connection.disconnect();
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
    protected Class<Void> handleUnsubscription(TwitchPubSub twitchPubSub, PubSubSubscription pubSubSubscription) {
        if (twitchPubSub == null) return null;
        twitchPubSub.unsubscribeFromTopic(pubSubSubscription);
        return Void.TYPE;
    }

    @Override
    protected PubSubRequest getRequestFromSubscription(PubSubSubscription subscription) {
        return subscription.getRequest();
    }

}
