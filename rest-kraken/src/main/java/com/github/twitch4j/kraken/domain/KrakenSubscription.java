package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class KrakenSubscription {

    @JsonProperty("_id")
    private String id;

    private Instant createdAt;

    private KrakenUser user;

    @JsonProperty("sub_plan")
    private String subscriptionPlan;

    @JsonProperty("sub_plan_name")
    private String subscriptionName;

}
