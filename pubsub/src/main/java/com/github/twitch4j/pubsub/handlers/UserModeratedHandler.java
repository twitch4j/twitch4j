package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.UserAutomodCaughtMessage;
import com.github.twitch4j.pubsub.events.UserAutomodCaughtMessageEvent;

import java.util.Collection;

class UserModeratedHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "user-moderation-notifications";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if (topicParts.length == 3 && "automod_caught_message".equalsIgnoreCase(message.getType())) {
            UserAutomodCaughtMessage data = TypeConvert.convertValue(message.getMessageData(), UserAutomodCaughtMessage.class);
            eventManager.publish(new UserAutomodCaughtMessageEvent(topicParts[1], topicParts[2], data));
            return true;
        }
        return false;
    }
}
