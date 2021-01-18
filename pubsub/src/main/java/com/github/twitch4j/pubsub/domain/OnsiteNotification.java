package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
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
    public static class Action {
        private String id;
        private String type;
        private String url;
        private String modalId;
        private String body;
        private String label;
    }

    @Data
    public static class Creator {
        private String userId;
        private String userName;
        private String thumbnailUrl;
    }
}
