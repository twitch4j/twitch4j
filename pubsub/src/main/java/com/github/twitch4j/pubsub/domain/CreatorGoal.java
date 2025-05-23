package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class CreatorGoal {
    private String id;
    private Type contributionType;
    private State state;
    private String description;
    private Integer currentContributions;
    private Integer targetContributions;
    private Instant createdAt;
    private String progressBarAccentColor;
    private String progressBarBackgroundColor;

    @Nullable
    @JsonProperty("shouldAutoIncrement")
    @Accessors(fluent = true)
    private Boolean shouldAutoIncrement;

    public enum Type {
        /**
         * Followers
         */
        FOLLOWERS,

        /**
         * Bits cheers
         */
        NEW_BITS,

        /**
         * New cheerers
         */
        NEW_CHEERERS,

        /**
         * Total subscriber count
         */
        SUBS,

        /**
         * Total subscriber points
         */
        SUB_POINTS,

        /**
         * New subscriber points
         */
        NEW_SUB_POINTS,

        /**
         * New subscribers
         */
        NEW_SUBS
    }

    public enum State {
        ACTIVE,
        FINISHED
    }
}
