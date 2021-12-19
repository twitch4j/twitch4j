package com.github.twitch4j.common.events.channel;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when a channel goes offline
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
@Deprecated
public class ChannelGoOfflineEvent extends TwitchEvent {

    /**
     * Channel
     */
    private final EventChannel channel;

    /**
     * Event Constructor
     *
     * @param channel The channel that went offline
     */
    public ChannelGoOfflineEvent(EventChannel channel) {
        this.channel = channel;
    }
}
