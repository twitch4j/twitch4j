package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.NONE)
public class LowTrustUser {

    private String id;

    private String lowTrustId;

    private String channelId;

    private AutomodCaughtMessage.Sender sender;

    private Instant evaluatedAt;

    private Instant updatedAt;

    private BanEvasionEvaluation banEvasionEvaluation;

    private LowTrustUserTreatment treatment;

    private SimpleUser updatedBy;

}
