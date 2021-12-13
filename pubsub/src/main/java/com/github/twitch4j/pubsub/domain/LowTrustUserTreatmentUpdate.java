package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.NONE)
public class LowTrustUserTreatmentUpdate {

    private String lowTrustId;

    private String channelId;

    private SimpleUser updatedBy;

    private Instant updatedAt;

    private String targetUserId;

    @JsonProperty("target_user")
    private String targetUserLogin;

    private LowTrustUserTreatment treatment;

    private BanEvasionEvaluation banEvasionEvaluation;

    private Instant evaluatedAt;

}
