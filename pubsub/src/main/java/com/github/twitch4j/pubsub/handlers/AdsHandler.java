package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.MidrollRequest;
import com.github.twitch4j.pubsub.events.MidrollRequestEvent;

import java.util.Collection;

class AdsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "ads";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("midroll_request".equals(message.getType())) {
            MidrollRequest midroll = TypeConvert.convertValue(message.getMessageData(), MidrollRequest.class);
            eventManager.publish(new MidrollRequestEvent(topicParts[topicParts.length - 1], midroll));
            return true;
        }
        return false;
    }
}
