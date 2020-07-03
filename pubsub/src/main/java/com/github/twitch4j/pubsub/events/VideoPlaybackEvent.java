package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.pubsub.domain.VideoPlaybackData;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class VideoPlaybackEvent extends TwitchEvent {
    String channelId;
    String channelName;
    @NonNull
    VideoPlaybackData data;

    public EventChannel getChannel() {
        return new EventChannel(channelId, channelName);
    }
}
