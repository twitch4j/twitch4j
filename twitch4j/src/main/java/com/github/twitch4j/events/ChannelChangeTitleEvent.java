package com.github.twitch4j.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.helix.domain.Stream;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * This event gets called when a channel changes the title
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ChannelChangeTitleEvent extends TwitchEvent {

    /**
     * The channel that changed their title
     */
    EventChannel channel;

    /**
     * The stream data
     */
    Stream stream;

    /**
     * The new stream title
     *
     * @return current title for the stream
     */
    public String getTitle() {
        return stream.getTitle();
    }

}
