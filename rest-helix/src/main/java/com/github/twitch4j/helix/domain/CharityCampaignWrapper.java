package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CharityCampaignWrapper {

    /**
     * A list that contains the charity campaign that the broadcaster is currently running.
     * <p>
     * The array is empty if the broadcaster is not running a charity campaign;
     * the campaign information is no longer available as soon as the campaign ends.
     */
    private List<CharityCampaign> data;

    /**
     * @return the currently running charity campaign, in an optional wrapper.
     */
    public Optional<CharityCampaign> getCurrentCampaign() {
        if (data == null || data.isEmpty()) return Optional.empty();
        return Optional.ofNullable(data.get(0));
    }

}
