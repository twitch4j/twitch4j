package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class LowTrustUserTreatmentUpdate {

    /**
     * An ID for the suspicious user entry, which is a combination of
     * the channel ID where the treatment was updated and the user ID of the suspicious user.
     */
    private String lowTrustId;

    /**
     * ID of the channel where the suspicious user was present.
     */
    private String channelId;

    /**
     * Information about the moderator who made any update for the suspicious user.
     */
    private SimpleUser updatedBy;

    /**
     * Timestamp of when the treatment was updated for the suspicious user.
     */
    private Instant updatedAt;

    /**
     * User ID of the suspicious user.
     */
    private String targetUserId;

    /**
     * Login of the suspicious user.
     */
    @JsonProperty("target_user")
    private String targetUserLogin;

    /**
     * The treatment set for the suspicious user.
     */
    private LowTrustUserTreatment treatment;

    /**
     * User types (if any) that apply to the suspicious user.
     */
    private List<LowTrustUserTypes> types;

    /**
     * A ban evasion likelihood value (if any) that has been applied to the user automatically by Twitch.
     */
    private BanEvasionEvaluation banEvasionEvaluation;

    /**
     * If applicable, the timestamp for the first time the suspicious user was automatically evaluated by Twitch.
     */
    private Instant evaluatedAt;

}
