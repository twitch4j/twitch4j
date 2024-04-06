package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AutomodSettingsUpdateEvent extends EventSubModeratorEvent {

    /**
     * The default AutoMod level for the broadcaster.
     * This field is null if the broadcaster has set one or more of the individual settings.
     */
    @Nullable
    private Integer overallLevel;

    /**
     * The Automod level for hostility involving name calling or insults.
     */
    private int bullying;

    /**
     * The Automod level for discrimination against disability.
     */
    private int disability;

    /**
     * The Automod level for racial discrimination.
     */
    private int raceEthnicityOrReligion;

    /**
     * The Automod level for discrimination against women.
     */
    private int misogyny;

    /**
     * The AutoMod level for discrimination based on sexuality, sex, or gender.
     */
    private int sexualitySexOrGender;

    /**
     * The Automod level for hostility involving aggression.
     */
    private int aggression;

    /**
     * The Automod level for sexual content.
     */
    private int sexBasedTerms;

    /**
     * The Automod level for profanity.
     */
    private int swearing;

}
