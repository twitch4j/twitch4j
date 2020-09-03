package com.github.twitch4j.extensions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationSegment {
    private Segment segment;
    private Record record;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Segment {
        private ConfigurationSegmentType segmentType;
        private String channelId;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Record {
        private String content;
        private String version;
    }
}
