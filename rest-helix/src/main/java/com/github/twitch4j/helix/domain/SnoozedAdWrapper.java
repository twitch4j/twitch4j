package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class SnoozedAdWrapper {

    /**
     * A list that contains information about the channel’s snoozes and next upcoming ad after successfully snoozing.
     */
    private List<SnoozedAd> data;

    /**
     * @return information about the channel’s snoozes and next upcoming ad after successfully snoozing
     */
    public SnoozedAd get() {
        if (data == null || data.isEmpty()) return null;
        return data.get(0);
    }

}
