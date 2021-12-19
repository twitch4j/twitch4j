package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when a user receives a raid.
 * <p>
 * This event is <i>not</i> called when a user receives a host that is not part of a raid.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class RaidEvent extends AbstractChannelEvent {

    /**
     * Event User who initiated the raid
     */
    private EventUser raider;

    /**
     * Number of viewers in the raid
     */
    private Integer viewers;

    /**
     * @param channel ChatChannel receiving the raid
     * @param raider  User who is sending the raid
     * @param viewers number of viewers from the raid
     */
    public RaidEvent(EventChannel channel, EventUser raider, Integer viewers) {
        super(channel);
        this.raider = raider;
        this.viewers = viewers;
    }
}
