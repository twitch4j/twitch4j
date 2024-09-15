package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

/**
 * Calls when a user pays forward a gift.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Unofficial
public class PayForwardEvent extends AbstractChannelEvent implements MirrorableEvent {

    /**
     * Raw Message Event
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    IRCMessageEvent messageEvent;

    /**
     * The user that is paying forward their gift.
     */
    EventUser user;

    /**
     * The previous user that gifted to this one, if not anonymous.
     */
    EventUser priorGifter;

    /**
     * The user that is receiving this gift, if it is not for the community at-large.
     */
    EventUser recipient;

    /**
     * Event Constructor
     *
     * @param event       The raw message event.
     * @param channel     The channel that this event originates from.
     * @param user        The user that is paying forward their gift.
     * @param priorGifter The previous user that gifted to this one, if not anonymous.
     * @param recipient   The user that is receiving this gift, if it is not for the community at-large.
     */
    @ApiStatus.Internal
    public PayForwardEvent(IRCMessageEvent event, EventChannel channel, EventUser user, EventUser priorGifter, EventUser recipient) {
        super(channel);
        this.messageEvent = event;
        this.user = user;
        this.priorGifter = priorGifter;
        this.recipient = recipient;
    }

    public boolean wasPriorGifterAnonymous() {
        return this.priorGifter == null;
    }

    public boolean isCommunityPayForward() {
        return this.recipient == null;
    }
}
