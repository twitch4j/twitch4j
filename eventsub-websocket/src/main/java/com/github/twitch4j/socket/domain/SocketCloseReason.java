package com.github.twitch4j.socket.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The reasons for closing the eventsub websocket connection.
 */
@RequiredArgsConstructor
public enum SocketCloseReason {

    /**
     * Indicates a problem with the server (similar to an HTTP 500 status code).
     */
    INTERNAL_SERVER_ERROR(4000),

    /**
     * Sending outgoing messages to the server is prohibited with the exception of pong messages.
     */
    CLIENT_SENT_INBOUND_TRAFFIC(4001),

    /**
     * You must respond to ping messages with a pong message.
     */
    CLIENT_FAILED_PING_PONG(4002),

    /**
     * When you connect to the server, you must create a subscription within 10 seconds or the connection is closed.
     * The time limit is subject to change.
     */
    CONNECTION_UNUSED(4003),

    /**
     * When you receive a session_reconnect message, you have 30 seconds to reconnect to the server and close the old connection.
     */
    RECONNECT_GRACE_TIME_EXPIRED(4004),

    /**
     * Transient network timeout.
     */
    NETWORK_TIMEOUT(4005),

    /**
     * Transient network error.
     */
    NETWORK_ERROR(4006),

    /**
     * The reconnect URL is invalid.
     */
    INVALID_RECONNECT(4007);

    public static final Map<Integer, SocketCloseReason> MAPPINGS;

    @Getter
    private final int code;

    static {
        MAPPINGS = Collections.unmodifiableMap(
            Arrays.stream(values())
                .collect(Collectors.toMap(SocketCloseReason::getCode, Function.identity()))
        );
    }
}
