package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class HypeTrainProgress {
    private HypeTrainLevel level;
    private Integer value;
    private Integer goal;
    private int progression;
    private Integer total;
    private Integer remainingSeconds;
    @JsonAlias("allTimeHighState")
    private String allTimeHighState; // e.g., "NONE", "APPROACHING", "REACHED"

    public boolean isAllTimeHigh() {
        return "REACHED".equals(allTimeHighState);
    }
}
