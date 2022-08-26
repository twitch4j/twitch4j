package com.github.twitch4j.helix.domain;

import com.github.twitch4j.common.util.DonationAmount;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * The charity campaign that the broadcaster is currently running.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CharityCampaign {

    /**
     * An ID that uniquely identifies the charity campaign.
     */
    private String id;

    /**
     * An ID that uniquely identifies the broadcaster that’s running the campaign.
     */
    private String broadcasterId;

    /**
     * The broadcaster’s display name.
     */
    private String broadcasterName;

    /**
     * The broadcaster’s login name.
     */
    private String broadcasterLogin;

    /**
     * The charity’s name.
     */
    private String charityName;

    /**
     * A description of the charity.
     */
    private String charityDescription;

    /**
     * A URL to an image of the charity’s logo.
     * <p>
     * The image’s type is PNG and its size is 100px X 100px.
     */
    private String charityLogo;

    /**
     * A URL to the charity’s website.
     */
    private String charityWebsite;

    /**
     * An object that contains the current amount of donations that the campaign has received.
     */
    private DonationAmount currentAmount;

    /**
     * An object that contains the amount of money that the campaign is trying to raise.
     * <p>
     * This field may be null if the broadcaster has not defined a target goal.
     */
    @Nullable
    private DonationAmount targetAmount;

}
