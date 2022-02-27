package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class ExtensionSecret {

    /**
     * Raw (base64-encoded) secret that should be used with JWT encoding.
     */
    private String content;

    /**
     * The earliest possible time this secret is valid to sign a JWT in RFC 3339 format.
     */
    private Instant activeAt;

    /**
     * The latest possible time this secret may be used to decode a JWT in RFC 3339 format.
     */
    private Instant expiresAt;

}
