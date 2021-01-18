package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubscriptionList {
    @JsonProperty("data")
    private List<Subscription> subscriptions;

    @JsonProperty("pagination")
    private HelixPagination pagination;

}
