package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.PollChoice;
import com.github.twitch4j.eventsub.domain.PollStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Poll {

    /**
     * ID of the poll.
     */
    private String id;

    /**
     * ID of the broadcaster.
     */
    private String broadcasterId;

    /**
     * Display name of the broadcaster.
     */
    private String broadcasterName;

    /**
     * Login name of the broadcaster.
     */
    private String broadcasterLogin;

    /**
     * Question displayed for the poll. Maximum: 60 characters.
     */
    private String title;

    /**
     * The poll choices. Minimum: 2 choices. Maximum: 5 choices.
     */
    @Singular
    private List<PollChoice> choices;

    /**
     * Indicates whether Bits can be used for voting. Default: false.
     *
     * @deprecated Twitch no longer supports bits on polls.
     */
    @Deprecated
    @Accessors(fluent = true)
    @JsonProperty("bits_voting_enabled")
    private Boolean isBitsVotingEnabled = false;

    /**
     * Number of Bits required to vote once with Bits. Minimum: 0. Maximum: 10000.
     *
     * @deprecated Twitch no longer supports bits on polls.
     */
    @Deprecated
    private Integer bitsPerVote;

    /**
     * Indicates whether Channel Points can be used for voting. Default: false.
     */
    @Accessors(fluent = true)
    @JsonProperty("channel_points_voting_enabled")
    private Boolean isChannelPointsVotingEnabled;

    /**
     * Number of Channel Points required to vote once with Channel Points. Minimum: 0. Maximum: 1000000.
     */
    private Integer channelPointsPerVote;

    /**
     * Poll status.
     */
    private PollStatus status;

    /**
     * Total duration for the poll (in seconds). Minimum: 15. Maximum: 1800.
     */
    @JsonProperty("duration")
    private Integer durationSeconds;

    /**
     * UTC timestamp for the poll’s start time.
     */
    private Instant startedAt;

    /**
     * UTC timestamp for the poll’s start time.
     */
    @Nullable
    private Instant endedAt;

    /**
     * @return the total duration for the poll.
     */
    @Nullable
    @JsonIgnore
    public Duration getDuration() {
        return durationSeconds != null ? Duration.ofSeconds(durationSeconds.longValue()) : null;
    }

}
