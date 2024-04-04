package com.github.twitch4j.eventsub.socket;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.EventSubTransport;
import com.github.twitch4j.eventsub.EventSubTransportMethod;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.function.Function;

public interface IEventSubSocket extends AutoCloseable {

    /**
     * @return the event manager for eventsub notifications
     */
    IEventManager getEventManager();

    /**
     * @return the default token to use when creating subscriptions
     */
    OAuth2Credential getDefaultToken();

    /**
     * Establishes a connection to the EventSub WebSocket server.
     *
     * @see WebsocketConnection#connect()
     */
    void connect();

    /**
     * Disconnects from the WebSocket server.
     *
     * @see WebsocketConnection#disconnect()
     */
    void disconnect();

    /**
     * Reconnects to the WebSocket server.
     *
     * @see WebsocketConnection#reconnect()
     */
    void reconnect();

    /**
     * @return the eventsub subscriptions associated with this socket
     */
    Collection<EventSubSubscription> getSubscriptions();

    /**
     * Creates (or schedules) an eventsub subscription for this socket.
     *
     * @param token the token to use for creating this subscription via helix
     * @param sub   the eventsub subscription to be registered
     * @return whether this subscription was not already registered to this pool
     * (and, if the websocket is already connected, whether the subscription was successful)
     */
    boolean register(OAuth2Credential token, EventSubSubscription sub);

    /**
     * Deletes an eventsub subscription from helix (and this socket).
     *
     * @param sub the eventsub subscription to be destroyed
     * @return whether the specified subscription was previously registered with this socket
     */
    boolean unregister(EventSubSubscription sub);

    /**
     * @return the most recently measured round-trip latency for the socket(s) in milliseconds, or -1 if unknown
     */
    long getLatency();

    /**
     * Creates (or schedules) an eventsub subscription for this socket, using {@link #getDefaultToken()}.
     *
     * @param sub the eventsub subscription to be created
     * @return whether this subscription was not already registered to this pool
     * (and, if the websocket is already connected, whether the subscription was successful)
     * @see #register(OAuth2Credential, EventSubSubscription)
     */
    default boolean register(EventSubSubscription sub) {
        return this.register(getDefaultToken(), sub);
    }

    /**
     * Helper method for {@link #register(EventSubSubscription)}
     */
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

    /**
     * Helper method for {@link #register(SubscriptionType, Function)}
     *
     * @deprecated in favor of {@link #register(SubscriptionType, Function)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    default <C extends EventSubCondition, B, E extends EventSubEvent> boolean register(SubscriptionType<C, B, E> type, C condition) {
        return this.register(type, (Function<B, C>) b -> condition);
    }

}
