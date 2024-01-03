package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * Called upon successfully joining a channel, or when a moderator updates a public chat room setting.
 * <p>
 * This event corresponds to {@code ROOMSTATE} over IRC.
 * <p>
 * When inspecting the updated setting values, it is easier to utilize these individual events:
 * {@link com.github.twitch4j.chat.events.roomstate.EmoteOnlyEvent},
 * {@link com.github.twitch4j.chat.events.roomstate.FollowersOnlyEvent},
 * {@link com.github.twitch4j.chat.events.roomstate.Robot9000Event},
 * {@link com.github.twitch4j.chat.events.roomstate.SlowModeEvent},
 * {@link com.github.twitch4j.chat.events.roomstate.SubscribersOnlyEvent}.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ChannelStateEvent extends AbstractChannelEvent {
    /**
     * The possible chat room settings that can be configured.
     */
    public enum ChannelState {
        /**
         * The language of the stream.
         *
         * @deprecated has not been sent since 2019-03-08.
         */
        @Deprecated
        BROADCAST_LANG,
        /**
         * Emote only chat configuration;
         * determines whether users can only send Twitch emotes in chat.
         */
        EMOTE,
        /**
         * Followers only chat configuration;
         * In minutes corresponding to how long a user must be following in order to chat.
         * Negative values indicate this setting is disabled.
         */
        FOLLOWERS,
        /**
         * Unique chat configuration (formerly Robot9000);
         * determines whether a user can repeat a previous message of theirs.
         */
        R9K,
        /**
         * Determines whether rituals are enabled on the channel.
         *
         * @see ChannelMessageEvent#isUserIntroduction()
         * @deprecated Twitch replaced this feature with the "user introduction" feature
         */
        @Unofficial
        @Deprecated
        RITUALS,
        /**
         * Slow mode chat configuration;
         * In seconds corresponding to minimum delay between an individual user's messages.
         */
        SLOW,
        /**
         * Subscribers-only chat configuration.
         */
        SUBSCRIBERS
    }

    /**
     * Contains the singular chat room setting that was updated
     * (or all chat room settings upon joining the channel).
     */
    Map<ChannelState, Object> states;

    /**
     * Event Constructor
     *
     * @param channel The channel that this event originates from.
     * @param state   The changed state triggering the event
     * @param value   The value representing the state
     * @deprecated unused by Twitch4J
     */
    @Deprecated
    public ChannelStateEvent(EventChannel channel, ChannelState state, Object value) {
        this(channel, Collections.singletonMap(state, value));
    }

    /**
     * Event Constructor
     *
     * @param channel The channel that this event originates from.
     * @param states  The chat room settings.
     */
    public ChannelStateEvent(EventChannel channel, Map<ChannelState, Object> states) {
        super(channel);
        this.states = Collections.unmodifiableMap(states);
    }

    /**
     * @param state a chat room setting to query
     * @return the latest value for the setting if it was just adjusted
     */
    @Nullable
    public Object getState(@NotNull ChannelState state) {
        return states.getOrDefault(state, null);
    }

}
