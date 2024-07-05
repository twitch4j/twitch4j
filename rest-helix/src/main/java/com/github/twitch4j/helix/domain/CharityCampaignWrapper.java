package com.github.twitch4j.helix.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CharityCampaignWrapper extends ValueWrapper<CharityCampaign> {

    /**
     * @return the currently running charity campaign, or empty if the broadcaster is not running a charity campaign.
     */
    public Optional<CharityCampaign> getCurrentCampaign() {
        return Optional.ofNullable(this.get());
    }

}
