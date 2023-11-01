package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.VideoPlaybackData;
import com.github.twitch4j.pubsub.events.VideoPlaybackEvent;

import java.util.Arrays;
import java.util.Collection;

class PlaybackHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "video-playback-by-id";
    }

    @Override
    public Collection<String> topicNames() {
        return Arrays.asList(topicName(), "video-playback");
    }

    @Override
    public TwitchEvent apply(Args args) {
        boolean hasId = args.getTopicParts()[0].endsWith("d");
        String lastTopicIdentifier = args.getLastTopicPart();
        VideoPlaybackData data = TypeConvert.jsonToObject(args.getRawMessage(), VideoPlaybackData.class);
        return new VideoPlaybackEvent(hasId ? lastTopicIdentifier : null, hasId ? null : lastTopicIdentifier, data);
    }
}
