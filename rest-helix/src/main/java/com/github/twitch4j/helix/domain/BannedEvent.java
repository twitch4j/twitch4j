package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class BannedEvent {
    /**
     * Event ID.
     */
    @NonNull
    private String id;

    /**
     * {@link EventType#BAN} or {@link EventType#UNBAN}.
     */
    private EventType eventType;

    /**
     * Timestamp for the event.
     */
    private Instant eventTimestamp;

    /**
     * Returns the version of the endpoint.
     */
    private String version;

    /**
     * @see EventData
     */
    private EventData eventData;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class EventData {
        /**
         * The id of the broadcaster where the event took place
         */
        @NonNull
        private String broadcasterId;

        /**
         * The login name of the broadcaster where the event took place
         */
        private String broadcasterLogin;

        /**
         * The display name of the broadcaster where the event took place
         */
        private String broadcasterName;

        /**
         * The id of the user that was banned/unbanned
         */
        @NonNull
        private String userId;

        /**
         * The login name of the user that was banned/unbanned
         */
        private String userLogin;

        /**
         * The display name of the user that was banned/unbanned
         */
        private String userName;

        /**
         * When the timeout expires, if applicable
         */
        private Instant expiresAt;

        /**
         * The reason for the ban if provided by the moderator.
         */
        @Nullable
        private String reason;

        /**
         * User ID of the moderator who initiated the ban.
         */
        private String moderatorId;

        /**
         * Login name of the moderator who initiated the ban.
         */
        private String moderatorLogin;

        /**
         * Display name of the moderator who initiated the ban.
         */
        private String moderatorName;
    }

    public enum EventType {
        BAN("moderation.user.ban"),
        UNBAN("moderation.user.unban");

        private final String type;

        EventType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }

        @JsonCreator
        public static EventType fromString(String type) {
            if (BAN.type.equalsIgnoreCase(type))
                return BAN;

            if (UNBAN.type.equalsIgnoreCase(type))
                return UNBAN;

            return null;
        }
    }
}
