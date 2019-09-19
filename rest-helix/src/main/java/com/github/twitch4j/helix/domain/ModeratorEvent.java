package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModeratorEvent {

    @NonNull
    private String id;

    @NonNull
    private String eventType;

    @NonNull
    private LocalDateTime eventTimestamp;

    @NonNull
    private String version;

    @NonNull
    private ModeratorEventData eventData;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModeratorEventData {

        @NonNull
        private String broadcasterId;

        @NonNull
        private String broadcasterName;

        @NonNull
        private String userId;

        @NonNull
        private String userName;
    }
}
