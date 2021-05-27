package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
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
        @JsonAlias("aggression")
        AGGRESSIVE,
        @JsonAlias("namecalling")
        BULLYING,
        @JsonAlias("ableism")
        DISABILITY,
        @JsonAlias({ "sexuality", "homophobia" })
        SEXUALITY_SEX_OR_GENDER,
        MISOGYNY,
        @JsonAlias("racism")
        RACE_ETHNICITY_OR_RELIGION,
        IDENTITY,
        @JsonAlias("sex_based_terms")
        SEXUAL,
        @JsonAlias("swearing")
        PROFANITY,
        @JsonEnumDefaultValue
        UNKNOWN
    }

}
