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
     * An ID that uniquely identifies the charity campaign.
     */
    @JsonAlias("id")
    private String campaignId;

    /**
     * The charity’s name.
     */
    @Unofficial
    private String charityName;

    /**
     * A URL to an image of the charity’s logo.
     */
    @Unofficial
    private String charityLogo;

    /**
     * An object that contains the amount of the user’s donation.
     */
    @JsonAlias("target_amount") // https://github.com/twitchdev/issues/issues/642
    private DonationAmount amount;

}
