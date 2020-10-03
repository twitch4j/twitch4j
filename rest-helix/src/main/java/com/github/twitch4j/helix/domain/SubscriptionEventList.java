package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionEventList {

    @JsonProperty("data")
    private List<SubscriptionEvent> subscriptionEvents;

    /**
     * A cursor value, to be used in a subsequent request to specify the starting point of the next set of results.
     */
    @JsonProperty("pagination")
    private HelixPagination pagination;

}
