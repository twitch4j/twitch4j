package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
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
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        boolean hasId = topicParts[0].endsWith("d");
        String lastTopicIdentifier = topicParts[topicParts.length - 1];
        VideoPlaybackData data = TypeConvert.jsonToObject(message.getRawMessage(), VideoPlaybackData.class);
        eventManager.publish(new VideoPlaybackEvent(hasId ? lastTopicIdentifier : null, hasId ? null : lastTopicIdentifier, data));
        return true;
    }
}
