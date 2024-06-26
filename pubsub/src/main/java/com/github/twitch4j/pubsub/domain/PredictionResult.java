package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class PredictionResult {

    /**
     * The result type (e.g., "WIN", "LOSE")
     */
    private String type;

    @Nullable
    private Integer pointsWon;

    @JsonProperty("is_acknowledged")
    @Accessors(fluent = true)
    private Boolean isAcknowledged;

}
