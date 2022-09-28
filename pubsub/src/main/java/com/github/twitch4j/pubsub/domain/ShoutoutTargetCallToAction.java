package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.AlternativeInstantDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ShoutoutTargetCallToAction {

    @Nullable
    @JsonProperty("NextSegment")
    private Segment nextSegment;

    @Nullable
    @JsonProperty("RecentlyStreamedCategories")
    private List<Category> recentCategories;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Category {
        @JsonProperty("Id")
        private String id;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("DisplayName")
        private String displayName;

        @JsonProperty("BoxArtURL")
        private String boxArtUrlTemplate; // contains {width} and {height} for template replacement
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Segment {
        @JsonProperty("Id")
        private String id;

        @JsonProperty("StartAt")
        @JsonDeserialize(using = AlternativeInstantDeserializer.class)
        private Instant startsAt;

        @JsonProperty("EndAt")
        @JsonDeserialize(using = AlternativeInstantDeserializer.class)
        private Instant endsAt;

        @JsonProperty("Title")
        private String title;

        @Accessors(fluent = true)
        @JsonProperty("IsCancelled")
        private Boolean isCancelled;

        @JsonProperty("Categories")
        private List<Category> categories;
    }

}
