package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Content classification tags that indicate that a stream may not be suitable for certain viewers.
 *
 * @see <a href="https://safety.twitch.tv/s/article/Content-Classification-Guidelines?language=en_US">Official Guidelines</a>
 */
public enum ContentClassification {

    /**
     * Excessive tobacco glorification or promotion, any marijuana consumption/use,
     * legal drug and alcohol induced intoxication, discussions of illegal drugs.
     */
    @JsonProperty("DrugsIntoxication")
    DRUGS,

    /**
     * Participating in online or in-person gambling, poker or fantasy sports,
     * that involve the exchange of real money.
     */
    @JsonProperty("Gambling")
    GAMBLING,

    /**
     * Games that are rated Mature or less suitable for a younger audience.
     * <p>
     * This tag is automatically applied based on the stream category.
     */
    @JsonProperty("MatureGame")
    MATURE,

    /**
     * Prolonged, and repeated use of obscenities, profanities, and vulgarities,
     * especially as a regular part of speech.
     */
    @JsonProperty("ProfanityVulgarity")
    PROFANITY,

    /**
     * Content that focuses on sexualized physical attributes and activities, sexual topics, or experiences.
     */
    @JsonProperty("SexualThemes")
    SEXUAL,

    /**
     * Simulations and/or depictions of realistic violence, gore, extreme injury, or death.
     */
    @JsonProperty("ViolentGraphic")
    VIOLENCE,

    /**
     * The channel has a content classification label that is unrecognized by the library;
     * Please file an issue on our GitHub repository.
     */
    @JsonEnumDefaultValue
    UNKNOWN;

}
