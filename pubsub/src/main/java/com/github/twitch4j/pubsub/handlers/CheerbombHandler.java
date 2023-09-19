package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.CheerbombData;
import com.github.twitch4j.pubsub.events.CheerbombEvent;

import java.util.Collection;

class CheerbombHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-cheer-events-public-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("cheerbomb".equalsIgnoreCase(message.getType())) {
            CheerbombData cheerbomb = TypeConvert.convertValue(message.getMessageData(), CheerbombData.class);
            eventManager.publish(new CheerbombEvent(topicParts[topicParts.length - 1], cheerbomb));
            return true;
        }
        return false;
    }
}
