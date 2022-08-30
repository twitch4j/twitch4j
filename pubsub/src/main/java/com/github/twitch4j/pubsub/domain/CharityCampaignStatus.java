package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CharityCampaignStatus extends CharityDonationData {

    @Accessors(fluent = true)
    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("campaign_name")
    private String charityName;

    @JsonProperty("campaign_description")
    private String charityDescription;

}
