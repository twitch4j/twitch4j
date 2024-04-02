package com.github.twitch4j.eventsub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Builder
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventSubTransport {

    /**
     * The transport method.
     */
    @NotNull
    private EventSubTransportMethod method;

    /**
     * The callback URL where the notification should be sent.
     * <p>
     * Specify this field only if method is set to webhook.
     */
    @Nullable
    private String callback;

    /**
     * The secret used for verifying a webhook signature.
     * <p>
     * The secret must be an ASCII string that’s a minimum of 10 characters long and a maximum of 100 characters long.
     * <p>
     * Specify this field only if method is set to webhook.
     */
    @Nullable
    private String secret;

    /**
     * An ID that identifies the conduit to send notifications to.
     * <p>
     * When you create a conduit, the server returns the conduit ID.
     * <p>
     * Specify this field only if method is set to conduit.
     */
    @Nullable
    private String conduitId;

    /**
     * An ID that identifies the WebSocket that notifications are sent to.
     * <p>
     * Specify this field only if method is set to websocket.
     */
    @With
    @Nullable
    @Setter(value = AccessLevel.PUBLIC, onMethod_ = { @Deprecated, @ApiStatus.Internal })
    private String sessionId;

    /**
     * The UTC date and time that the WebSocket connection was established.
     * <p>
     * Included only if method is set to websocket.
     */
    @Nullable
    private Instant connectedAt;

    /**
     * The UTC date and time that the WebSocket connection was lost.
     * <p>
     * Included only if method is set to websocket.
     */
    @Nullable
    private Instant disconnectedAt;

}
