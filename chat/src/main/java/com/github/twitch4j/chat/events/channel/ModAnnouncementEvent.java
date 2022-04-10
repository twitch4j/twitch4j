package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.enums.AnnouncementColor;
import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Fired when the broadcaster or a moderator publishes an accented message via {@code /announce}.
 * <p>
 * Since this event is not officially documented in the IRC guide, it could change or stop working at any time.
 *
 * @see <a href="https://twitter.com/TwitchSupport/status/1509634525982302208">Official Announcement</a>
 */
@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Unofficial
public class ModAnnouncementEvent extends AbstractChannelEvent {

    /**
     * The raw message event.
     */
    @ToString.Exclude
    IRCMessageEvent messageEvent;

    /**
     * The user that made the announcement.
     */
    EventUser announcer;

    /**
     * The message being announced.
     */
    String message;

    /**
     * The color accent for the announcement.
     */
    AnnouncementColor color;

    /**
     * Event Constructor
     *
     * @param messageEvent The raw message event.
     * @param channel      The channel that this event originates from.
     * @param announcer    The user that made the announcement.
     * @param message      The message being announced.
     * @param color        The color accent for the announcement.
     */
    public ModAnnouncementEvent(IRCMessageEvent messageEvent, EventChannel channel, EventUser announcer, String message, AnnouncementColor color) {
        super(channel);
        this.messageEvent = messageEvent;
        this.announcer = announcer;
        this.message = message;
        this.color = color;
    }

}
