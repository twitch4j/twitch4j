package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.DonationAmount;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Optional;

@Data
@Setter(AccessLevel.PRIVATE)
public class CharityDonationData {

    private String campaignId;

    private String campaignCurrency = "USD";

    private Long donationTotal;

    private Long goalTarget;

    public DonationAmount getTotal() {
        return new DonationAmount(donationTotal, 2, campaignCurrency);
    }

    public Optional<DonationAmount> getTarget() {
        return Optional.ofNullable(goalTarget)
            .filter(l -> l > 0)
            .map(target -> new DonationAmount(target, 2, campaignCurrency));
    }

}
