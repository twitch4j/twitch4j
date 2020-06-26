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
public class OnsiteNotification {
    private String userId;
    private String id;
    private String body;
    private String bodyMd;
    private String type; // e.g. "streamup"
    private String renderStyle;
    private String thumbnailUrl;
    private List<Action> actions;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean read;
    private List<Creator> creators;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Action {
        private String id;
        private String type;
        private String url;
        private String modalId;
        private String body;
        private String label;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Creator {
        private String userId;
        private String userName;
        private String thumbnailUrl;
    }
}
