package com.github.twitch4j.socket;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.EventSubTransport;
import com.github.twitch4j.eventsub.EventSubTransportMethod;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;

import java.util.Collection;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface IEventSubSocket extends AutoCloseable {

    IEventManager getEventManager();

    OAuth2Credential getDefaultToken();

    void connect();

    void disconnect();

    void reconnect();

    Collection<EventSubSubscription> getSubscriptions();

    boolean register(OAuth2Credential token, EventSubSubscription sub);

    boolean unregister(EventSubSubscription sub);

    default boolean register(EventSubSubscription sub) {
        return this.register(getDefaultToken(), sub);
    }

    default <C extends EventSubCondition, B, E extends EventSubEvent> boolean register(SubscriptionType<C, B, E> type, Function<B, C> conditions) {
        return this.register(
            type.prepareSubscription(
                conditions,
                EventSubTransport.builder()
                    .method(EventSubTransportMethod.WEBSOCKET)
                    .sessionId("")
                    .build()
            )
        );
    }

    default <C extends EventSubCondition, B, E extends EventSubEvent> boolean register(SubscriptionType<C, B, E> type, C condition) {
        return this.register(type, (Function<B, C>) b -> condition);
    }

}
