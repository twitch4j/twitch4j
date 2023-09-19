package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelMessageEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;

/**
 * This event gets called when an action message (/me text) is received in a channel.
 */
public class ChannelMessageActionEvent extends AbstractChannelMessageEvent {

    /**
     * Event Constructor
     *
     * @param channel      The channel that this event originates from.
     * @param messageEvent The raw message event
     * @param user         The user who triggered the event.
     * @param message      The plain text of the message.
     */
    public ChannelMessageActionEvent(
        EventChannel channel,
        IRCMessageEvent messageEvent,
        EventUser user,
        String message
    ) {
        super(channel, messageEvent, user, message);
    }

}
