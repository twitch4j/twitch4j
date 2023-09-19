package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.AutomodCaughtMessageData;
import com.github.twitch4j.pubsub.events.AutomodCaughtMessageEvent;

import java.util.Collection;

class AutoModQueueHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "automod-queue";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if (topicParts.length == 3 && "automod_caught_message".equalsIgnoreCase(message.getType())) {
            AutomodCaughtMessageData data = TypeConvert.convertValue(message.getMessageData(), AutomodCaughtMessageData.class);
            eventManager.publish(new AutomodCaughtMessageEvent(topicParts[2], data));
            return true;
        }
        return false;
    }
}
