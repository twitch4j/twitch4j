package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeConductor {
    private String id;
    private String source;
    private List<HypeTrainParticipation> participation;
    private HypeTrainConductorUser user;
}
