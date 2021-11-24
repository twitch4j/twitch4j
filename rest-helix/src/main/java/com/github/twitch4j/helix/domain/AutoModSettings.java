package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutoModSettings {

    /**
     * The broadcaster’s ID.
     */
    private String broadcasterId;

    /**
     * The moderator’s ID.
     */
    private String moderatorId;

    /**
     * The default AutoMod level for the broadcaster.
     * This field is null if the broadcaster has set one or more of the individual settings.
     */
    @Nullable
    private Integer overallLevel;

    /**
     * The Automod level for hostility involving aggression.
     */
    private Integer aggression;

    /**
     * The Automod level for hostility involving name calling or insults.
     */
    private Integer bullying;

    /**
     * The Automod level for discrimination against disability.
     */
    private Integer disability;

    /**
     * The Automod level for discrimination against women.
     */
    private Integer misogyny;

    /**
     * The Automod level for racial discrimination.
     */
    private Integer raceEthnicityOrReligion;

    /**
     * The Automod level for sexual content.
     */
    private Integer sexBasedTerms;

    /**
     * The AutoMod level for discrimination based on sexuality, sex, or gender.
     */
    private Integer sexualitySexOrGender;

    /**
     * The Automod level for profanity.
     */
    private Integer swearing;

    /**
     * @return whether the settings are valid to be passed to {@link com.github.twitch4j.helix.TwitchHelix#updateChatSettings(String, String, String, ChatSettings)}
     */
    public boolean isValidConfiguration() {
        Integer[] levels = { aggression, bullying, disability, misogyny, raceEthnicityOrReligion, sexBasedTerms, sexualitySexOrGender, swearing };
        boolean individual = Arrays.stream(levels).anyMatch(Objects::nonNull);

        if (individual) {
            // When individual settings are configured, the overall level must be unset
            if (overallLevel != null)
                return false;

            // Each level must be within [0, 4]
            for (Integer level : levels) {
                if (level != null && (level < 0 || level > 4))
                    return false;
            }
        }

        return true;
    }

}
