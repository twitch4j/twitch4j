package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookSubscriptionList {
    
    @JsonProperty("data")
    private List<WebhookSubscription> subscriptions;
    
    /**
     * Total number of items returned.
     * <p>
     * If only from_id was in the request, this is the total number of followed users.
     * If only to_id was in the request, this is the total number of followers.
     * If both from_id and to_id were in the request, this is 1 (if the "from" user follows the "to" user) or 0.
     */
    private Integer total;
    
    @JsonProperty("pagination")
    private HelixPagination pagination;
    
}
