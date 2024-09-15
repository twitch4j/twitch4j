package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

/**
 * This event gets called when a user receives a raid.
 * <p>
 * This event is <i>not</i> called when a user receives a host that is not part of a raid.
 */
@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RaidEvent extends AbstractChannelEvent implements MirrorableEvent {

    /**
     * Raw Message Event
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    IRCMessageEvent messageEvent;

    /**
     * Event User who initiated the raid
     */
    EventUser raider;

    /**
     * Number of viewers in the raid
     */
    Integer viewers;

    /**
     * Inbound Raid Event Constructor
     *
     * @param event   Raw Message Event
     * @param channel ChatChannel receiving the raid
     * @param raider  User who is sending the raid
     * @param viewers number of viewers from the raid
     */
    @ApiStatus.Internal
    public RaidEvent(IRCMessageEvent event, EventChannel channel, EventUser raider, Integer viewers) {
        super(channel);
        this.messageEvent = event;
        this.raider = raider;
        this.viewers = viewers;
    }
}
