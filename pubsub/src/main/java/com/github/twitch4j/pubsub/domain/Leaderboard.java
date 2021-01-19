package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.NanoInstantDeserializer;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Leaderboard {
    private Identifier identifier;
    private List<Entry> top;
    private Context entryContext;
    private Event event;

    @Data
    public static class Identifier {
        private String domain;
        private String groupingKey;
        private String timeAggregationUnit;
        private String timeBucket;
    }

    @Data
    public static class Entry {
        private Integer rank;
        private Long score;
        private String entryKey;
    }

    @Data
    public static class Context {
        private Entry entry;
        private List<Entry> context;
    }

    @Data
    public static class Event {
        private String domain;
        private String id;
        @JsonDeserialize(using = NanoInstantDeserializer.class)
        private Instant timeOfEvent;
        private String groupingKey;
        private String entryKey;
        private Long eventValue;
    }
}
