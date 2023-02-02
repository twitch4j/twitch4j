package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class CharityDonationList {

    /**
     * The donations that users have made to the broadcaster’s charity campaign.
     * <p>
     * This is empty if the broadcaster is not currently running a charity campaign.
     * The donation information is not available after the campaign ends.
     */
    @JsonProperty("data")
    private List<CharityCampaignDonation> donations;

    /**
     * An object that contains the information used to page through the list of results.
     */
    private HelixPagination pagination;

}
