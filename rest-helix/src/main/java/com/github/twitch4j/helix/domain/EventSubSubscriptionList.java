package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.EventSubSubscription;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class EventSubSubscriptionList {

    /**
     * Subscriptions
     */
    @JsonProperty("data")
    private List<EventSubSubscription> subscriptions;

    /**
     * Total number of subscriptions for the client ID that made the subscription creation request.
     */
    private Integer total;

    /**
     * Subscription limit for client id that made the subscription creation request.
     */
    private Integer limit;

    /**
     * A cursor value to be used in a subsequent request to specify the starting point of the next set of results.
     */
    private HelixPagination pagination;

}
