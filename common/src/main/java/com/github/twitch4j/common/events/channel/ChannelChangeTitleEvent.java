package com.github.twitch4j.common.events.channel;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when a channel changes the game
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
@Deprecated
public class ChannelChangeTitleEvent extends TwitchEvent {

    /**
     * Channel
     */
    private final EventChannel channel;

    /**
     * Title
     */
    private final String title;

    /**
     * Event Constructor
     *
     * @param channel        The channel that went live
     * @param title          The stream title
     */
    public ChannelChangeTitleEvent(EventChannel channel, String title) {
        this.channel = channel;
        this.title = title;
    }
}
