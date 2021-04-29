package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
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
    public static class ModeratorEventData {

        /**
         * The ID of the channel where the event took place
         */
        @NonNull
        private String broadcasterId;

        /**
         * Login of the broadcaster.
         */
        @NonNull
        private String broadcasterLogin;

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
         * Login of the user.
         */
        @NonNull
        private String userLogin;

        /**
         * The (display) name of the moderator being added/removed
         */
        @NonNull
        private String userName;
    }
}
