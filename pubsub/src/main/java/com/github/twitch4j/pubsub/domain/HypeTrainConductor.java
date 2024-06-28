package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class HypeTrainConductor {
    private String id;
    private String source;
    private HypeTrainConductorUser user;
    private HypeTrainParticipations participations;
}
