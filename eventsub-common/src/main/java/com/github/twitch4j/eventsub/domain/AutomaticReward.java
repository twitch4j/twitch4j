package com.github.twitch4j.eventsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomaticReward {

    /**
     * The type of reward.
     */
    private Type type;

    /**
     * The reward cost.
     */
    private int cost;

    /**
     * The emote that was unlocked.
     */
    @Nullable
    private SimpleEmote unlockedEmote;

    public enum Type {
        SINGLE_MESSAGE_BYPASS_SUB_MODE,
        SEND_HIGHLIGHTED_MESSAGE,
        RANDOM_SUB_EMOTE_UNLOCK,
        CHOSEN_SUB_EMOTE_UNLOCK,
        CHOSEN_MODIFIED_SUB_EMOTE_UNLOCK
    }
}
