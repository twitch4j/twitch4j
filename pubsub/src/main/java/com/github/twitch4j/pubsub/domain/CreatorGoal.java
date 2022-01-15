package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class CreatorGoal {
    private String id;
    private Type contributionType;
    private State state;
    private String description;
    private Integer currentContributions;
    private Integer targetContributions;

    public enum Type {
        FOLLOWERS,
        SUB_POINTS,
        NEW_SUB_POINTS
    }

    public enum State {
        ACTIVE,
        FINISHED
    }
}
