package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class RaidRequest {

    /**
     * The UTC date and time, in RFC3339 format, when the raid request was created.
     */
    private Instant createdAt;

    /**
     * Whether the channel being raided contains mature content.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_mature")
    private Boolean isMature;

}
