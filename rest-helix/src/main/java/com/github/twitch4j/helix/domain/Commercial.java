package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commercial {
    /**
     * Length of the triggered commercial
     */
    private Integer length;

    /**
     * Provides contextual information on why the request failed
     */
    private String message;

    /**
     * Seconds until the next commercial can be served on this channel
     */
    private Integer retryAfter;
}
