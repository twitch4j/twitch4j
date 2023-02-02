package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.util.DonationAmount;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelCharityDonateEvent extends EventSubUserChannelEvent {

    /**
     * An ID that identifies the donation.
     * The ID is unique across campaigns.
     */
    private String id;

    /**
     * An ID that uniquely identifies the charity campaign.
     */
    private String campaignId;

    /**
     * The charity’s name.
     */
    private String charityName;

    /**
     * A URL to an image of the charity’s logo.
     */
    private String charityLogo;

    /**
     * A description of the charity.
     */
    private String charityDescription;

    /**
     * A URL to the charity’s website.
     */
    private String charityWebsite;

    /**
     * An object that contains the amount of the user’s donation.
     */
    @JsonAlias("target_amount") // https://github.com/twitchdev/issues/issues/642
    private DonationAmount amount;

}
