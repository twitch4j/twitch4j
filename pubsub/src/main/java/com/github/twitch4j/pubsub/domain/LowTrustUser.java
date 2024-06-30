package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class LowTrustUser {

    /**
     * The user ID of the suspicious user.
     */
    private String id;

    /**
     * Unique ID for this low trust chat message.
     */
    private String lowTrustId;

    /**
     * ID of the channel where the suspicious user was present.
     */
    private String channelId;

    /**
     * Information about the suspicious user.
     */
    private AutomodCaughtMessage.Sender sender;

    /**
     * If applicable, the timestamp for the first time the suspicious user was automatically evaluated by Twitch.
     */
    private Instant evaluatedAt;

    /**
     * The timestamp of when the treatment was updated for the suspicious user.
     */
    private Instant updatedAt;

    /**
     * A ban evasion likelihood value (if any) that has been applied to the user automatically by Twitch.
     */
    private BanEvasionEvaluation banEvasionEvaluation;

    /**
     * The treatment set for the suspicious user.
     */
    private LowTrustUserTreatment treatment;

    /**
     * Information about the moderator who made any update for the suspicious user.
     */
    private SimpleUser updatedBy;

    /**
     * A list of channel IDs where the suspicious user is also banned.
     */
    @Nullable
    private List<String> sharedBanChannelIds;

    /**
     * User types (if any) that apply to the suspicious user.
     */
    private List<LowTrustUserTypes> types;

}
