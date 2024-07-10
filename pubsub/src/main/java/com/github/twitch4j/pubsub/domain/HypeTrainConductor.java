package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

@Data
@Setter(onMethod_ = { @Deprecated })
public class HypeTrainConductor {
    private String id;
    private String source;
    private HypeTrainConductorUser user;
    private HypeTrainParticipations participations;
}
