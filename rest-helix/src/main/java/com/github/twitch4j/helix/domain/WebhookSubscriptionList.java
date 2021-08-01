package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Webhook Subscription List
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Deprecated
public class WebhookSubscriptionList {

    @JsonProperty("data")
    private List<WebhookSubscription> subscriptions;

    /**
     * A hint at the total number of results returned, on all pages.
     * <p>
     * Note this is an approximation: as you page through the list, some subscriptions may expire and others may be added.
     */
    private Integer total;

    /**
     * A cursor value, to be used in a subsequent request to specify the starting point of the next set of results.
     * If this is empty, you are at the last page.
     */
    private HelixPagination pagination;

}
