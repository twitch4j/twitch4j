package com.github.twitch4j.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.helix.domain.Stream;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * This event gets called when a channel changes the game
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ChannelChangeGameEvent extends TwitchEvent {

    /**
     * The channel that changed their game
     */
    EventChannel channel;

    /**
     * The stream data
     */
    Stream stream;

    /**
     * The new stream gameId
     *
     * @return id of the game
     */
    public String getGameId() {
        return stream.getGameId();
    }

}
