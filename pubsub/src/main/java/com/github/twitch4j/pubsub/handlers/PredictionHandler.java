package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.events.PredictionCreatedEvent;
import com.github.twitch4j.pubsub.events.PredictionUpdatedEvent;

import java.util.Collection;

class PredictionHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "predictions-channel-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("event-created".equals(message.getType())) {
            eventManager.publish(TypeConvert.convertValue(message.getMessageData(), PredictionCreatedEvent.class));
            return true;
        } else if ("event-updated".equals(message.getType())) {
            eventManager.publish(TypeConvert.convertValue(message.getMessageData(), PredictionUpdatedEvent.class));
            return true;
        }
        return false;
    }
}
