package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PredictionOutcome {

    /**
     * The outcome ID.
     */
    private String id;

    /**
     * The outcome title. Maximum: 25 characters.
     */
    private String title;

    /**
     * The color for the outcome.
     */
    private PredictionColor color;

    /**
     * The number of users who used Channel Points on this outcome.
     */
    private Integer users;

    /**
     * The total number of Channel Points used on this outcome.
     */
    private Long channelPoints;

    /**
     * The users who used the most Channel Points on this outcome.
     */
    @Nullable
    private List<Predictor> topPredictors;

}
