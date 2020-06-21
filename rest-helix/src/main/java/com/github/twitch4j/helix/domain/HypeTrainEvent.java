package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HypeTrainEvent {
    /**
     * The distinct ID of the event
     */
    @NonNull
    private String id;

    /**
     * Displays hypetrain.{event_name}
     * <p>
     * Note: Currently only "hypetrain.progression"
     */
    private String eventType;

    /**
     * Timestamp of the event
     */
    private Instant eventTimestamp;

    /**
     * Returns the version of the endpoint
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
         * Channel ID of which Hype Train events the clients are interested in
         */
        @NonNull
        private String broadcasterId;

        /**
         * Timestamp of when another Hype Train can be started again
         */
        private Instant cooldownEndTime;

        /**
         * Timestamp of the expiration time of this Hype Train
         */
        private Instant expiresAt;

        /**
         * The goal value of the level above
         */
        private Long goal;

        /**
         * The total score so far towards completing the level goal above
         */
        private Long total;

        /**
         * The distinct ID of this Hype Train
         */
        @NonNull
        private String id;

        /**
         * An object that represents the most recent contribution
         */
        private Contribution lastContribution;

        /**
         * The highest level (in the scale of 1-5) reached of the Hype Train
         */
        private Integer level;

        /**
         * Timestamp of when this Hype Train started
         */
        private Instant startedAt;

        /**
         * Top contribution objects, one object for each type
         * <p>
         * For example, one object would represent top contributor of BITS, by aggregate, and one would represent top contributor of SUBS by count
         */
        private List<Contribution> topContributions;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contribution {
        /**
         * Total amount contributed.
         * <p>
         * If type is BITS, total represents amounts of bits used
         * If type is SUBS, total is 500, 1000, or 2500 to represent tier 1, 2, or 3 subscriptions respectively
         */
        @NonNull
        private Long total;

        /**
         * Identifies the contribution method, either BITS or SUBS
         */
        @NonNull
        private Type type;

        /**
         * ID of the contributing user
         */
        @NonNull
        @JsonProperty("user")
        private String userId;

        public enum Type {
            BITS,
            SUBS
        }
    }
}
