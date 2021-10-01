package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubscriptionList {

    /**
     * The subscriptions.
     */
    @JsonProperty("data")
    private List<Subscription> subscriptions;

    /**
     * A cursor value, to be used in a subsequent request to specify the starting point of the next set of results. If this is empty, you are at the last page.
     */
    private HelixPagination pagination;

    /**
     * The number of Twitch users subscribed to the broadcaster.
     */
    private Integer total;

    /**
     * The current number of subscriber points earned by this broadcaster.
     * <p>
     * Points are based on the subscription tier of each user that subscribes to this broadcaster.
     * For example, a Tier 1 subscription is worth 1 point, Tier 2 is worth 2 points, and Tier 3 is worth 6 points.
     */
    private Integer points;

}
