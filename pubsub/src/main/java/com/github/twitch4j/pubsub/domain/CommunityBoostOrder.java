package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class CommunityBoostOrder {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("State")
    private String state; // e.g. DELIVERING_ORDER

    @JsonProperty("GoalProgress")
    private Integer goalProgress;

    @JsonProperty("GoalTarget")
    private Integer goalTarget;

}
