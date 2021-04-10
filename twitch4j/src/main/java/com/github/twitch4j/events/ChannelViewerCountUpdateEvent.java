package com.github.twitch4j.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.helix.domain.Stream;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ChannelViewerCountUpdateEvent extends TwitchEvent {

    /**
     * The channel that changed in viewer count.
     */
    EventChannel channel;

    /**
     * The stream data.
     */
    Stream stream;

    /**
     * @return the new viewer count.
     */
    public Integer getViewerCount() {
        return stream.getViewerCount();
    }

}
