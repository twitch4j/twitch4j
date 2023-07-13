package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.github.twitch4j.util.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Content classification tags that indicate that a stream may not be suitable for certain viewers.
 *
 * @see <a href="https://safety.twitch.tv/s/article/Content-Classification-Guidelines?language=en_US">Official Guidelines</a>
 */
@RequiredArgsConstructor
public enum ContentClassification {

    /**
     * Excessive tobacco glorification or promotion, any marijuana consumption/use,
     * legal drug and alcohol induced intoxication, discussions of illegal drugs.
     */
    DRUGS("DrugsIntoxication"),

    /**
     * Participating in online or in-person gambling, poker or fantasy sports,
     * that involve the exchange of real money.
     */
    GAMBLING("Gambling"),

    /**
     * Games that are rated Mature or less suitable for a younger audience.
     * <p>
     * This tag is automatically applied based on the stream category.
     */
    MATURE_GAME("MatureGame"),

    /**
     * Prolonged, and repeated use of obscenities, profanities, and vulgarities,
     * especially as a regular part of speech.
     */
    PROFANITY("ProfanityVulgarity"),

    /**
     * Content that focuses on sexualized physical attributes and activities, sexual topics, or experiences.
     */
    SEXUAL("SexualThemes"),

    /**
     * Simulations and/or depictions of realistic violence, gore, extreme injury, or death.
     */
    VIOLENCE("ViolentGraphic"),

    /**
     * The channel has a content classification label that is unrecognized by the library;
     * Please file an issue on our GitHub repository.
     */
    @JsonEnumDefaultValue
    UNKNOWN("Unknown");

    private static final Map<String, ContentClassification> MAPPINGS = EnumUtil.buildMapping(values());

    private final String twitchString;

    @Override
    public String toString() {
        return this.twitchString;
    }

    @NotNull
    @ApiStatus.Internal
    public static ContentClassification parse(@NotNull String id) {
        return MAPPINGS.getOrDefault(id, UNKNOWN);
    }
}
