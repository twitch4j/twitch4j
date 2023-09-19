package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.events.UserPredictionMadeEvent;
import com.github.twitch4j.pubsub.events.UserPredictionResultEvent;

import java.util.Collection;

class UserPredictionHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "predictions-user-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("prediction-made".equals(message.getType())) {
            eventManager.publish(TypeConvert.convertValue(message.getMessageData(), UserPredictionMadeEvent.class));
            return true;
        } else if ("prediction-result".equals(message.getType())) {
            eventManager.publish(TypeConvert.convertValue(message.getMessageData(), UserPredictionResultEvent.class));
            return true;
        }
        return false;
    }
}
