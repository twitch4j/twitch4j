package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventData {
        /**
         * The id of the broadcaster where the event took place
         */
        @NonNull
        private String broadcasterId;

        /**
         * The name of the broadcaster where the event took place
         */
        private String broadcasterName;

        /**
         * The id of the user that was banned/unbanned
         */
        @NonNull
        private String userId;

        /**
         * The name of the user that was banned/unbanned
         */
        private String userName;

        /**
         * When the timeout expires, if applicable
         */
        private Instant expiresAt;
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
