package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.github.twitch4j.common.annotation.Unofficial;
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

    @Unofficial
    public enum Category {

        /**
         * Threatening, inciting, or promoting violence or other harm.
         */
        @JsonAlias("aggression")
        AGGRESSIVE,

        /**
         * Name-calling, insults, or antagonization.
         */
        @JsonAlias("namecalling")
        BULLYING,

        /**
         * Demonstrating hatred or prejudice based on perceived or actual mental or physical abilities.
         */
        @JsonAlias("ableism")
        DISABILITY,

        /**
         * Demonstrating hatred or prejudice based on sexual identity, sexual orientation, gender identity, or gender expression.
         */
        @JsonAlias({ "sexuality", "homophobia" })
        SEXUALITY_SEX_OR_GENDER,

        /**
         * Demonstrating hatred or prejudice against women, including sexual objectification.
         */
        @JsonAlias("sexism")
        MISOGYNY,

        /**
         * Demonstrating hatred or prejudice based on race, ethnicity, or religion.
         */
        @JsonAlias({ "racism", "religion" })
        RACE_ETHNICITY_OR_RELIGION,

        /**
         * @deprecated This is an old AutoMod category; use {@link #DISABILITY}, {@link #SEXUALITY_SEX_OR_GENDER}, {@link #MISOGYNY}, or {@link #RACE_ETHNICITY_OR_RELIGION} instead.
         */
        @Deprecated
        IDENTITY,

        /**
         * Sexual acts, anatomy.
         */
        @JsonAlias("sex_based_terms")
        SEXUAL,

        /**
         * Swear words, &*^!#@%*.
         */
        @JsonAlias("swearing")
        PROFANITY,

        /**
         * Unable to parse the AutoMod category into a known type; please report on our Discord or GitHub page.
         */
        @JsonEnumDefaultValue
        UNKNOWN

    }

}
