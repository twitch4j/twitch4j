package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public abstract class ChannelCharityCampaignMetaEvent extends EventSubChannelEvent {

    /**
     * An ID that identifies the charity campaign.
     */
    @JsonAlias("id")
    private String campaignId;

    /**
     * The charity’s name.
     */
    private String charityName;

    /**
     * A URL to an image of the charity’s logo.
     * <p>
     * The image’s type is PNG and its size is 100px X 100px.
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
     * An object that contains the current amount of donations that the campaign has received.
     */
    private DonationAmount currentAmount;

    /**
     * An object that contains the campaign’s target fundraising goal.
     */
    private DonationAmount targetAmount;

}
