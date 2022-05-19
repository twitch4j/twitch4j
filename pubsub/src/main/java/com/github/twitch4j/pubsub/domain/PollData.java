package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
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
    public static class PollSettings {
        private Setting multiChoice;
        private Setting subscriberOnly;
        private Setting subscriberMultiplier;
        private Setting bitsVotes;
        private Setting channelPointsVotes;

        @Data
        public static class Setting {
            private Boolean isEnabled;
            private Long cost;
        }
    }

    @Data
    public static class PollChoice {
        private String choiceId;
        private String title;
        private Votes votes;
        private Tokens tokens;
        private Integer totalVoters;
    }

    @Data
    public static class Votes {
        private Long total;
        private Long bits;
        private Long channelPoints;
        private Long base;
    }

    @Data
    public static class Tokens {
        private Long bits;
        private Long channelPoints;
    }

    @Data
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
        ARCHIVED,
        TERMINATED,
        MODERATED,
        @JsonEnumDefaultValue
        INVALID
    }
}
