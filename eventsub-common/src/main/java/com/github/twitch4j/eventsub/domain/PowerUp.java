package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.enums.TwitchEnum;
import com.github.twitch4j.common.util.TwitchEnumDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class PowerUp {

    /**
     * The type of Power-up used.
     */
    @NotNull
    @JsonDeserialize(using = TwitchEnumDeserializer.class)
    private TwitchEnum<PowerUpType> type;

    /**
     * Optional: Emote associated with the reward.
     */
    @Nullable
    private SimpleEmote emote;

    /**
     * Optional: The ID of the message effect.
     * Possible values include "cosmic-abyss", "simmer", and "rainbow-eclipse".
     */
    @Nullable
    private String messageEffectId;

}
