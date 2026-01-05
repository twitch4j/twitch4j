package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.Contribution;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeTrain {

    /**
     * The Hype Train ID.
     */
    private String id;

    /**
     * The broadcaster ID.
     */
    private String broadcasterUserId;

    /**
     * The broadcaster login.
     */
    private String broadcasterUserLogin;

    /**
     * The broadcaster display name.
     */
    private String broadcasterUserName;

    /**
     * The current level of the Hype Train.
     */
    private int level;

    /**
     * Total points contributed to the Hype Train.
     */
    private int total;

    /**
     * The number of points contributed to the Hype Train at the current level.
     */
    private int progress;

    /**
     * The number of points required to reach the next level.
     */
    private int goal;

    /**
     * The contributors with the most points contributed.
     */
    private List<Contribution> topContributions;

    /**
     * The broadcasters participating in the shared Hype Train.
     * Null if the Hype Train is not shared.
     */
    private @Nullable List<SharedChatParticipant> sharedTrainParticipants;

    /**
     * The time when the Hype Train started.
     */
    private Instant startedAt;

    /**
     * The time when the Hype Train expires.
     * The expiration is extended when the Hype Train reaches a new level.
     */
    private Instant expiresAt;

    /**
     * The type of the Hype Train.
     */
    private Type type;

    /**
     * Indicates if the Hype Train is shared.
     * When true, {@link #getSharedTrainParticipants()} will contain the list of broadcasters the train is shared with.
     */
    private @JsonProperty("is_shared_train") boolean isSharedTrain;

    /**
     * @see <a href="https://help.twitch.tv/s/article/hype-train-guide?language=en_US#special">Official Help Article</a>
     */
    public enum Type {

        /**
         * After reaching Level 5 of the Treasure Train, a 35% discount will be unlocked on purchases of 5 or more Tier 1 single-month gift subscriptions.
         */
        TREASURE,

        /**
         * Viewers will receive the Golden Kappa emote regardless of what level they contributed at,
         * so long as the Hype Train reaches at least Level 1.
         */
        GOLDEN_KAPPA,

        /**
         * A regular hype train.
         */
        REGULAR

    }

}
