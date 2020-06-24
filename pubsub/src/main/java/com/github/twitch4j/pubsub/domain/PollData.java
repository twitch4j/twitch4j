package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollData {
    private String pollId;
    private String ownedBy;
    private String createdBy;
    private String title;
    private Instant startedAt;
    private Instant endedAt;
    private String endedBy;
    private Long durationSeconds;
    private PollSettings settings;
    private Status status;
    private List<PollChoice> choices;
    private Votes votes;
    private Tokens tokens;
    private Integer totalVoters;
    private Long remainingDurationMilliseconds;
    private Contributor topContributor;
    private Contributor topBitsContributor;
    private Contributor topChannelPointsContributor;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PollSettings {
        private Setting multiChoice;
        private Setting subscriberOnly;
        private Setting subscriberMultiplier;
        private Setting bitsVotes;
        private Setting channelPointsVotes;

        @Data
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Setting {
            private Boolean isEnabled;
            private Long cost;
        }
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PollChoice {
        private String choiceId;
        private String title;
        private Votes votes;
        private Tokens tokens;
        private Integer totalVoters;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Votes {
        private Long total;
        private Long bits;
        private Long channelPoints;
        private Long base;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tokens {
        private Long bits;
        private Long channelPoints;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contributor {
        private String userId;
        private String displayName;
        private Long bitsContributed;
        private Long channelPointsContributed;
    }

    @SuppressWarnings("unused")
    public enum Status {
        ACTIVE,
        COMPLETED,
        ARCHIVED
    }
}
