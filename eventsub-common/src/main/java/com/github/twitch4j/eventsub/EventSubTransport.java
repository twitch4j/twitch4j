package com.github.twitch4j.eventsub;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class EventSubTransport {

    /**
     * The transport method.
     */
    private EventSubTransportMethod method;

    /**
     * The callback URL where the notification should be sent.
     */
    private String callback;

    /**
     * The secret used for verifying a signature.
     */
    private String secret;

}
