package com.github.twitch4j.extensions.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationSegment {
    private Segment segment;
    private Record record;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Segment {
        private ConfigurationSegmentType segmentType;
        private String channelId;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record {
        private String content;
        private String version;
    }
}
