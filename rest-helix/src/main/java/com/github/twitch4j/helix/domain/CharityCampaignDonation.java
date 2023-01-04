package com.github.twitch4j.helix.domain;

import com.github.twitch4j.common.util.DonationAmount;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class CharityCampaignDonation {

    /**
     * An ID that identifies the charity campaign that the donation applies to.
     */
    private String campaignId;

    /**
     * An ID that identifies a user that donated money to the campaign.
     */
    private String userId;

    /**
     * The login name of a user that donated money to the campaign.
     */
    private String userLogin;

    /**
     * The display name of a user that donated money to the campaign.
     */
    private String userName;

    /**
     * An object that contains the amount of money that the user donated.
     */
    private DonationAmount amount;

}
