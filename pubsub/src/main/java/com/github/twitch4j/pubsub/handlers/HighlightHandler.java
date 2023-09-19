package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.events.ChatHighlightEvent;

import java.util.Collection;

class HighlightHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-chat-highlights";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("chat-highlight".equals(message.getType())) {
            eventManager.publish(TypeConvert.convertValue(message.getMessageData(), ChatHighlightEvent.class));
            return true;
        }
        return false;
    }
}
