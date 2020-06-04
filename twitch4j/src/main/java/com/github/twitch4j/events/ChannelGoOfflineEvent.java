package com.github.twitch4j.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * This event gets called when a channel goes offline
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ChannelGoOfflineEvent extends TwitchEvent {

    /**
     * The channel that went offline
     */
    EventChannel channel;

}
