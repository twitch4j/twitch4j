package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class PredictionOutcome {
    private String id;
    private PredictionColor color;
    private String title;
    private Integer totalPoints;
    private Integer totalUsers;
    private List<Prediction> topPredictors;
}
