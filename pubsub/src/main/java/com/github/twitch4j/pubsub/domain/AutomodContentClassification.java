package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodContentClassification {

    /**
     * Category that the message was classified as.
     */
    private Category category;

    /**
     * The level that the message was classified as.
     */
    private Integer level;

    public enum Category {
        // New Categories
        AGGRESSION,
        BULLYING,
        DISABILITY,
        MISOGYNY,
        RACE_ETHNICITY_OR_RELIGION,
        SEXUALITY_SEX_OR_GENDER,
        SEX_BASED_TERMS,
        SWEARING,
        // Old Categories
        AGGRESSIVE,
        IDENTITY,
        PROFANITY,
        SEXUAL,
        // Other
        @JsonEnumDefaultValue UNKNOWN;

        private static final Category[] VALUES = values();

        public static Category fromString(String category) {
            for (Category cat : VALUES) {
                if (cat.name().equalsIgnoreCase(category))
                    return cat;
            }

            return UNKNOWN;
        }
    }

}
