package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChannelPointsGain {
    private String userId;
    private String channelId;
    private Integer totalPoints;
    private Integer baselinePoints;
    private String reasonCode;
    private List<PointGainMultiplier> multipliers;

    @Data
    public static class PointGainMultiplier {
        private String reasonCode;
        private Double factor;
    }
}
