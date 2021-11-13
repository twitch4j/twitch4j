package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Setter(AccessLevel.PRIVATE)
public class CommunityPointsGoal {

    private String id;
    private String channelId;
    private String title;
    private String description;
    private Integer goalAmount;
    private Integer pointsContributed;
    private Integer smallContribution;
    private Integer perStreamMaximumUserContribution;
    private Integer durationDays;
    private Instant startedAt;
    private Instant endedAt;
    private String backgroundColor;
    private ChannelPointsReward.Image defaultImage;

    @Nullable
    private ChannelPointsReward.Image image;

    @Accessors(fluent = true)
    @JsonProperty("is_in_stock")
    private Boolean isInStock;

    /**
     * The type of community points goal.
     * <p>
     * For example: "BOOST", "CREATOR"
     */
    private String goalType;

    /**
     * The status of the community points goal.
     * <p>
     * For example: "STARTED"
     */
    private String status;

    public Type getParsedType() {
        return Type.MAPPINGS.getOrDefault(getGoalType(), Type.UNKNOWN);
    }

    public Status getParsedStatus() {
        return Status.MAPPINGS.getOrDefault(getStatus(), Status.UNKNOWN);
    }

    public enum Type {
        BOOST, CREATOR, @JsonEnumDefaultValue UNKNOWN;

        static final Map<String, Type> MAPPINGS = Arrays.stream(values()).collect(Collectors.toMap(Enum::toString, Function.identity()));
    }

    public enum Status {
        ARCHIVED, ENDED, FULFILLED, STARTED, @JsonEnumDefaultValue UNKNOWN, UNSTARTED;

        static final Map<String, Status> MAPPINGS = Arrays.stream(values()).collect(Collectors.toMap(Enum::toString, Function.identity()));
    }

}
