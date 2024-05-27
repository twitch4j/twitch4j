package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomaticReward {

    /**
     * The type of reward.
     */
    @JsonAlias("reward_type") // official changelog uses a different name for this field than the eventsub reference
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

        /**
         * "Send a Message in Sub-Only Mode" was redeemed.
         */
        SINGLE_MESSAGE_BYPASS_SUB_MODE,

        /**
         * "Highlight My Message" was redeemed.
         */
        SEND_HIGHLIGHTED_MESSAGE,

        /**
         * "Unlock a Random Sub Emote" was redeemed.
         */
        RANDOM_SUB_EMOTE_UNLOCK,

        /**
         * "Choose an Emote to Unlock" was redeemed.
         */
        CHOSEN_SUB_EMOTE_UNLOCK,

        /**
         * "Modify a Single Emote" was redeemed.
         */
        CHOSEN_MODIFIED_SUB_EMOTE_UNLOCK,

        /**
         * "Animate My Message" was redeemed.
         */
        @ApiStatus.Experimental // twitch feature is not in production yet (and could be reverted)
        SEND_ANIMATED_MESSAGE,

        /**
         * Send a gigantified emote was redeemed.
         */
        @ApiStatus.Experimental // twitch feature is not in production yet (and could be reverted)
        SEND_GIGANTIFIED_EMOTE,

        /**
         * Trigger a celebration alert was redeemed.
         */
        @ApiStatus.Experimental // twitch feature is not in production yet (and could be reverted)
        CELEBRATION,

        /**
         * An unrecognized automatic reward type; please report to our issue tracker.
         */
        @JsonEnumDefaultValue
        OTHER

    }
}
