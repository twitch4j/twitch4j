package me.philippheuer.twitch4j.message.pubsub.topics;

import lombok.Getter;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.enums.PubSubTopic;

import java.util.Optional;

@Getter
public class VideoPlayback extends PubSubTopics {
    private final Channel channel;

    public VideoPlayback(Channel channel) {
        super(PubSubTopic.VIDEO_PLAYBACK, Optional.empty());
        this.channel = channel;
    }

    @Override
    public String stringify() {
        return String.format("%s.%s", getTopic().getPrefix(), channel.getName());
    }
}
