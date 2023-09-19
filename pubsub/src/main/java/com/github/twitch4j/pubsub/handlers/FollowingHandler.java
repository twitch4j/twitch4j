package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.FollowingData;
import com.github.twitch4j.pubsub.events.FollowingEvent;

import java.util.Collection;

class FollowingHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "following";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        FollowingData data = TypeConvert.jsonToObject(message.getRawMessage(), FollowingData.class);
        eventManager.publish(new FollowingEvent(topicParts[topicParts.length - 1], data));
        return true;
    }
}
