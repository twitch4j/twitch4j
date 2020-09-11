package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModeratorEvent {

    /**
     * Event ID
     */
    @NonNull
    private String id;

    /**
     * Displays "moderation.user.ban" or "moderation.user.unban".
     */
    @NonNull
    private String eventType;

    /**
     * RFC3339 formatted timestamp for events.
     */
    @NonNull
    @JsonProperty("event_timestamp")
    private Instant timestamp;

    /**
     * Returns the version of the endpoint.
     */
    @NonNull
    private String version;

    /**
     * @see ModeratorEventData
     */
    @NonNull
    private ModeratorEventData eventData;

    /**
     * @return the timestamp of when the event took place
     * @deprecated in favor of getTimestamp
     */
    @JsonIgnore
    @Deprecated
    public LocalDateTime getEventTimestamp() {
        return LocalDateTime.ofInstant(timestamp, ZoneOffset.UTC);
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModeratorEventData {

        /**
         * The ID of the channel where the event took place
         */
        @NonNull
        private String broadcasterId;

        /**
         * The (display) name of the channel where the event took place
         */
        @NonNull
        private String broadcasterName;

        /**
         * The User ID of the moderator being added/removed
         */
        @NonNull
        private String userId;

        /**
         * The (display) name of the moderator being added/removed
         */
        @NonNull
        private String userName;
    }
}
