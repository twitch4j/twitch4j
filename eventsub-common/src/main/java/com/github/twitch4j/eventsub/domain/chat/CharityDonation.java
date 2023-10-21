package com.github.twitch4j.eventsub.domain.chat;

import com.github.twitch4j.common.util.DonationAmount;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class CharityDonation {

    /**
     * Name of the charity.
     */
    private String charityName;

    /**
     * An object that contains the amount of money that the user paid.
     */
    private DonationAmount amount;

}
