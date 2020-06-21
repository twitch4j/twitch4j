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
    /**
     * The name of the Cheermote (e.g., "Cheer", "PogChamp", "Kappa")
     */
    private String prefix;

    /**
     * Cheermotes with their metadata
     */
    private List<Tier> tiers;

    /**
     * The emote type
     */
    private Type type;

    /**
     * Order of the emotes as shown in the bits card, in ascending order
     */
    private Integer order;

    /**
     * The date when this Cheermote was last updated
     */
    private Instant lastUpdated;

    /**
     * Indicates whether or not this emote provides a charity contribution match
     */
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
        /**
         * ID of the emote tier. Possible tiers are: 1, 100, 500, 1000, 5000, 10k, or 100k
         */
        @NonNull
        private String id;

        /**
         * Minimum number of bits needed to be used to hit the given tier of emote
         */
        private Long minBits;

        /**
         * Hex code for the color associated with the bits of that tier
         */
        private String color;

        /**
         * Structure containing both animated and static image sets, sorted by light and dark
         */
        private ThemedImages images;

        /**
         * Whether or not emote information is accessible to users
         */
        private Boolean canCheer;

        /**
         * Whether or not the emote is hidden from the bits card
         */
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
        /**
         * Image at 1.0x
         */
        @JsonProperty("1")
        private String size10;

        /**
         * Image at 1.5x
         */
        @JsonProperty("1.5")
        private String size15;

        /**
         * Image at 2.0x
         */
        @JsonProperty("2")
        private String size20;

        /**
         * Image at 3.0x
         */
        @JsonProperty("3")
        private String size30;

        /**
         * Image at 4.0x
         */
        @JsonProperty("4")
        private String size40;
    }
}
