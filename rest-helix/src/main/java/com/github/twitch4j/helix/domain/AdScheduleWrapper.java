package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class AdScheduleWrapper {

    /**
     * A list that contains information related to the channel’s ad schedule.
     */
    private List<AdSchedule> data;

    /**
     * @return information related to the channel’s ad schedule
     */
    public AdSchedule get() {
        if (data == null || data.isEmpty()) return null;
        return data.get(0);
    }

}
