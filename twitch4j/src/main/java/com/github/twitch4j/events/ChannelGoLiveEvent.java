package com.github.twitch4j.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.helix.domain.Stream;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * This event gets called when a channel goes live
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ChannelGoLiveEvent extends TwitchEvent {

    /**
     * The channel that went live
     */
    EventChannel channel;

    /**
     * The stream data
     */
    Stream stream;

}
