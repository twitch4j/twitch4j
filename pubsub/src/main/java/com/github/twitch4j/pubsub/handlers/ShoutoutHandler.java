package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.CreateShoutoutData;
import com.github.twitch4j.pubsub.events.ShoutoutCreatedEvent;

import java.util.Collection;

class ShoutoutHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "shoutout";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("create".equalsIgnoreCase(message.getType())) {
            CreateShoutoutData shoutoutInfo = TypeConvert.convertValue(message.getMessageData(), CreateShoutoutData.class);
            eventManager.publish(new ShoutoutCreatedEvent(topicParts[topicParts.length - 1], shoutoutInfo));
            return true;
        }
        return false;
    }
}
