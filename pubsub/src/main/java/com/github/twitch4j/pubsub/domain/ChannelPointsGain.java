package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(onMethod_ = { @Deprecated })
public class ChannelPointsGain {
    private String userId;
    private String channelId;
    private Integer totalPoints;
    private Integer baselinePoints;
    private String reasonCode;
    private List<PointGainMultiplier> multipliers;

    @Data
    @Setter(onMethod_ = { @Deprecated })
    public static class PointGainMultiplier {
        private String reasonCode;
        private Double factor;
    }
}
