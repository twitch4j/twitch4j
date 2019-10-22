package com.github.twitch4j.common.events.channel;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when a channel goes live
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelGoLiveEvent extends TwitchEvent {

    /**
     * Channel
     */
    private final EventChannel channel;

    /**
     * Title
     */
    private final String title;

    /**
     * GameId
     */
    private final String gameId;

    /**
     * Event Constructor
     *
     * @param channel        The channel that went live
     * @param title          The stream title
     * @param gameId         The gameId
     */
    public ChannelGoLiveEvent(EventChannel channel, String title, String gameId) {
        this.channel = channel;
        this.title = title;
        this.gameId = gameId;
    }
}
