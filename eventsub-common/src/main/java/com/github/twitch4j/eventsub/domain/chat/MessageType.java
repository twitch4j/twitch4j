package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import org.jetbrains.annotations.ApiStatus;

/**
 * EventSub equivalent of the {@code msg-id} IRC tag.
 */
public enum MessageType {

    /**
     * A normal chat message.
     */
    TEXT,

    /**
     * Channel points were used to highlight the chat message.
     */
    CHANNEL_POINTS_HIGHLIGHTED,

    /**
     * Channel points were used to send this message in sub-only mode.
     */
    CHANNEL_POINTS_SUB_ONLY,

    /**
     * The user designated this message as their introduction to the community.
     *
     * @see <a href="https://twitter.com/TwitchSupport/status/1481008097749573641">Twitch Announcement</a>
     */
    USER_INTRO,

    /**
     * Channel points were used to animate the chat message.
     */
    @ApiStatus.Experimental // twitch feature is not in production yet (and could be reverted)
    ANIMATED,

    /**
     * Channel points were used to attach a gigantic emote to the chat message.
     */
    @ApiStatus.Experimental // twitch feature is not in production yet (and could be reverted)
    GIGANTIFIED_EMOTE,

    /**
     * An unrecognized message type; please report to our issue tracker.
     */
    @JsonEnumDefaultValue
    OTHER

}
