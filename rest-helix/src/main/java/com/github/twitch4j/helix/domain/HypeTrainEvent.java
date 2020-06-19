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

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HypeTrainEvent {
    @NonNull
    private String id;
    private String eventType;
    private String eventTimestamp;
    private String version;
    private EventData eventData;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventData {
        @NonNull
        private String broadcasterId;
        private String cooldownEndTime;
        private String expiresAt;
        private Long goal;
        @NonNull
        private String id;
        private Contribution lastContribution;
        private Integer level;
        private String startedAt;
        private List<Contribution> topContributions;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contribution {
        @NonNull
        private Long total;

        @NonNull
        private Type type;

        @NonNull
        @JsonProperty("user")
        private String userId;

        public enum Type {
            BITS,
            SUBS
        }
    }
}
