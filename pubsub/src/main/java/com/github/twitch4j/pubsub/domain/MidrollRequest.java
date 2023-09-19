package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class MidrollRequest {
    private String commercialId;
    private Integer jitterBuckets;
    private Long jitterTime;
    private Long warmupTime;
    private List<Double> weightedBuckets;
}
