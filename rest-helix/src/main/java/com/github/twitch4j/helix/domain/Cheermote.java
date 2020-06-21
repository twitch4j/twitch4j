package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cheermote {
    private String prefix;
    private List<Tier> tiers;
    private Type type;
    private Integer order;
    private Instant lastUpdated;
    private Boolean isCharitable;

    public enum Type {
        GLOBAL_FIRST_PARTY,
        GLOBAL_THIRD_PARTY,
        CHANNEL_CUSTOM,
        DISPLAY_ONLY,
        SPONSORED;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

        private static final Map<String, Type> MAPPINGS = Arrays.stream(Type.values()).collect(Collectors.toMap(Type::toString, Function.identity()));

        @JsonCreator
        public static Type fromString(String type) {
            return MAPPINGS.get(type);
        }
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tier {
        @NonNull
        private String id;
        private Long minBits;
        private String color;
        private ThemedImages images;
        private Boolean canCheer;
        private Boolean showInBitsCard;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ThemedImages {
        private ImageSet dark;
        private ImageSet light;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageSet {
        @JsonProperty("animated")
        private SizedImages animatedImages;

        @JsonProperty("static")
        private SizedImages staticImages;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SizedImages {
        @JsonProperty("1")
        private String size10;
        @JsonProperty("1.5")
        private String size15;
        @JsonProperty("2")
        private String size20;
        @JsonProperty("3")
        private String size30;
        @JsonProperty("4")
        private String size40;
    }
}
