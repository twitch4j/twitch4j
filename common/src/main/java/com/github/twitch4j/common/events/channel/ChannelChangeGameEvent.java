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
public class ChannelChangeGameEvent extends TwitchEvent {

    /**
     * Channel
     */
    private final EventChannel channel;

    /**
     * GameId
     */
    private final String gameId;

    /**
     * Event Constructor
     *
     * @param channel        The channel that went live
     * @param gameId         The gameId
     */
    public ChannelChangeGameEvent(EventChannel channel, String gameId) {
        this.channel = channel;
        this.gameId = gameId;
    }
}
