package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatModerationAction {

    /**
     * The raw string for the class of moderation action. Can be "chat_channel_moderation" or "chat_login_moderation"
     *
     * @see ChatModerationAction#getModType()
     */
    private String type;

    /**
     * The specific moderation action that took place, in a convenient enum form
     */
    private ModerationAction moderationAction;

    /**
     * The arguments passed to the moderation action command. Can be null.
     *
     * @see ChatModerationAction#getTargetedUserName()
     * @see ChatModerationAction#getReason()
     * @see ChatModerationAction#getDeletedMessage()
     * @see ChatModerationAction#getTimeoutDuration()
     * @see ChatModerationAction#getSlowDuration()
     * @see ChatModerationAction#getFollowersDuration()
     */
    private List<String> args;

    /**
     * The login for the account that triggered the moderation action
     */
    private String createdBy;

    /**
     * The user id for the account that triggered the moderation action
     */
    private String createdByUserId;

    /**
     * The relevant message id, if the action was a message deletion
     */
    private String msgId;

    /**
     * The user id for the targeted account by the moderation action (e.g. the user id that was banned)
     */
    private String targetUserId;

    /**
     * The user name for the targeted account by the moderation action (e.g. the user login that was banned)
     * <p>
     * Note: While this field is included in the pubsub response, the API appears to never populate it with a non-empty string
     */
    private String targetUserLogin;

    /**
     * Whether the moderation action was triggered by AutoMod
     *
     * @see <a href="https://help.twitch.tv/s/article/setting-up-moderation-for-your-twitch-channel?language=en_US#automod">Twitch Docs</a>
     */
    private Boolean fromAutomod;

    /*
     * Library-generated helper fields/methods
     */

    /**
     * The specific moderation type that took place, in a convenient enum form
     */
    @Getter(lazy = true)
    private final ModerationType modType = ModerationType.parse(type, moderationAction);

    /**
     * @return optional wrapper around the username that was specified in a ban, unban, timeout, untimeout, vip,
     * unvip, mod, unmod, host, raid, or delete message command
     */
    public Optional<String> getTargetedUserName() {
        final ModerationAction action = getModerationAction();
        if (args != null && args.size() > 0 && (action == ModerationAction.BAN || action == ModerationAction.UNBAN
            || action == ModerationAction.TIMEOUT || action == ModerationAction.UNTIMEOUT
            || action == ModerationAction.DELETE || action == ModerationAction.HOST
            || action == ModerationAction.VIP || action == ModerationAction.UNVIP
            || action == ModerationAction.MOD || action == ModerationAction.UNMOD
            || action == ModerationAction.RAID))
            return Optional.of(args.get(0));

        return Optional.ofNullable(this.targetUserLogin).filter(s -> !s.isEmpty());
    }

    /**
     * @return optional wrapper around the reason associated with the ban or timeout (or an empty string if none was specified)
     */
    public Optional<String> getReason() {
        switch (getModerationAction()) {
            case BAN:
                if (args != null && args.size() > 1)
                    return Optional.of(args.get(1));

            case TIMEOUT:
                if (args != null && args.size() > 2)
                    return Optional.of(args.get(2));

            default:
                return Optional.empty();
        }
    }

    /**
     * @return optional wrapper around the message that was deleted
     */
    public Optional<String> getDeletedMessage() {
        if (getModerationAction() == ModerationAction.DELETE && args != null && args.size() > 1)
            return Optional.of(args.get(1));

        return Optional.empty();
    }

    /**
     * @return optional wrapper around the duration the user was timed out for, in seconds
     */
    public OptionalInt getTimeoutDuration() {
        if (getModerationAction() == ModerationAction.TIMEOUT && args != null && args.size() > 1)
            return OptionalInt.of(Integer.parseInt(args.get(1)));

        return OptionalInt.empty();
    }

    /**
     * @return optional wrapper around the slow mode delay, in seconds
     */
    public OptionalInt getSlowDuration() {
        if (getModerationAction() == ModerationAction.SLOW && args != null && args.size() > 0)
            return OptionalInt.of(Integer.parseInt(args.get(0)));

        return OptionalInt.empty();
    }

    /**
     * @return optional wrapper around the duration followers only mode was set to, in minutes
     */
    public OptionalInt getFollowersDuration() {
        if (getModerationAction() == ModerationAction.FOLLOWERS && args != null && args.size() > 0)
            return OptionalInt.of(Integer.parseInt(args.get(0)));

        return OptionalInt.empty();
    }

    /*
     * Convenience Enums
     */

    public enum ModerationType {
        CHANNEL,
        LOGIN;

        private static ModerationType parse(String twitchString, ModerationAction modAction) {
            if ("chat_channel_moderation".equals(twitchString))
                return CHANNEL;

            if ("chat_login_moderation".equals(twitchString))
                return LOGIN;

            // API inconsistency documented here https://github.com/twitchdev/issues/issues/99
            if (modAction == ModerationAction.MOD || modAction == ModerationAction.UNMOD
                || modAction == ModerationAction.VIP || modAction == ModerationAction.UNVIP)
                return LOGIN;

            return null; // should not occur
        }
    }

    public enum ModerationAction {
        /**
         * User was banned
         */
        BAN,
        /**
         * User was unbanned
         */
        UNBAN,
        /**
         * User was timed out
         */
        TIMEOUT,
        /**
         * User time out was removed
         */
        UNTIMEOUT,
        /**
         * Chat message was deleted
         */
        DELETE,
        /**
         * Chat slow mode was enabled
         */
        SLOW,
        /**
         * Chat slow mode was disabled
         */
        SLOW_OFF,
        /**
         * Chat followers only mode was enabled
         */
        FOLLOWERS,
        /**
         * Chat followers only mode was disabled
         */
        FOLLOWERS_OFF,
        /**
         * Unique chat mode was enabled
         */
        R9K_BETA,
        /**
         * Unique chat mode was disabled
         */
        R9K_BETA_OFF,
        /**
         * Emote only chat was enabled
         */
        EMOTE_ONLY,
        /**
         * Emote only chat was disabled
         */
        EMOTE_ONLY_OFF,
        /**
         * Subscribers-only chat was enabled
         */
        SUBSCRIBERS,
        /**
         * Subscribers-only chat was disabled
         */
        SUBSCRIBERS_OFF,
        /**
         * User was given VIP status
         */
        VIP,
        /**
         * User VIP status was removed
         */
        UNVIP,
        /**
         * User was modded
         */
        MOD,
        /**
         * User was unmodded
         */
        UNMOD,
        /**
         * Another channel was hosted
         */
        HOST,
        /**
         * Channel exited host mode
         */
        UNHOST,
        /**
         * A raid on another channel was initiated
         */
        RAID,
        /**
         * Channel exited raid mode
         */
        UNRAID,
        /**
         * The message was flagged by AutoMod for manual review
         */
        AUTOMOD_REJECTED("automod_rejected"),
        /**
         * Moderator added a permitted term to AutoMod
         */
        ADD_PERMITTED_TERM("add_permitted_term"),
        /**
         * Moderator added a blocked term to AutoMod
         */
        ADD_BLOCKED_TERM("add_blocked_term"),
        /**
         * Moderator deleted a permitted term from AutoMod
         */
        DELETE_PERMITTED_TERM("delete_permitted_term"),
        /**
         * Moderator deleted a blocked term from AutoMod
         */
        DELETE_BLOCKED_TERM("delete_blocked_term"),
        /**
         * Moderator approved a message that was flagged by AutoMod
         */
        APPROVED_AUTOMOD_MESSAGE("approved_automod_message"),
        /**
         * Moderator denied a message that was flagged by AutoMod
         */
        DENIED_AUTOMOD_MESSAGE("denied_automod_message"),
        /**
         * AutoMod settings were modified
         */
        MODIFIED_AUTOMOD_PROPERTIES("modified_automod_properties");

        @Getter
        private final String twitchString;

        ModerationAction() {
            this.twitchString = this.name().toLowerCase().replace("_", "");
        }

        ModerationAction(String twitchString) {
            this.twitchString = twitchString;
        }

        private static final Map<String, ModerationAction> MAPPINGS = Arrays.stream(ModerationAction.values())
            .collect(Collectors.toMap(ModerationAction::getTwitchString, Function.identity()));

        @JsonCreator
        public static ModerationAction fromString(String action) {
            return MAPPINGS.get(action);
        }
    }
}
