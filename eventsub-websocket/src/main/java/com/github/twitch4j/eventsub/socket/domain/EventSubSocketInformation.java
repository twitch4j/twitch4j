package com.github.twitch4j.eventsub.socket.domain;

import com.github.twitch4j.eventsub.socket.enums.SocketMessageType;
import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import com.github.twitch4j.eventsub.socket.enums.EventSubSocketStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class EventSubSocketInformation {

    /**
     * An ID that uniquely identifies this WebSocket connection.
     * <p>
     * Use this ID to set the session_id field in all subscription requests.
     */
    private String id;

    /**
     * The connection’s status.
     * <p>
     * Upon {@link SocketMessageType#SESSION_WELCOME}, this will be set to {@link EventSubSocketStatus#CONNECTED}.
     */
    private EventSubSocketStatus status;

    /**
     * The maximum number of seconds that you should expect silence before receiving a keepalive message.
     * <p>
     * For a welcome message, this is the number of seconds that you have to subscribe to an event after receiving the welcome message.
     * If you don’t subscribe to an event within this window, the socket is disconnected.
     * <p>
     * When {@link #getStatus()} is {@link EventSubSocketStatus#RECONNECTING}, this is set to null.
     */
    @Nullable
    private Integer keepaliveTimeoutSeconds;

    /**
     * The UTC date and time that the connection was created.
     */
    private Instant connectedAt;

    /**
     * The URL to reconnect to if you get a {@link SocketMessageType#SESSION_RECONNECT} message.
     * <p>
     * Note: {@link TwitchEventSocket} automatically follows these reconnect URLs.
     */
    @Nullable
    private String reconnectUrl;

}
