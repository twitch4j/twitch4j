package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class MaxPerStream {

    /**
     * Is the setting enabled.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    /**
     * The max per stream limit.
     */
    @JsonAlias("max_per_stream")
    private Integer value;

}
