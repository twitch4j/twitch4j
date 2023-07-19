package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Called following a /vips message.
 *
 * @deprecated Twitch will decommission this event on February 18, 2023; migrate to TwitchHelix#getChannelVips
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class ListVipsEvent extends AbstractChannelEvent {

    /**
     * The login names of the VIPs of this channel.
     */
    List<String> vips;

    /**
     * Event Constructor
     *
     * @param channel The channel that this event originates from.
     * @param vips    The login names of the VIPs of this channel.
     */
    public ListVipsEvent(@NonNull EventChannel channel, @NonNull List<String> vips) {
        super(channel);
        this.vips = vips;
    }

}
