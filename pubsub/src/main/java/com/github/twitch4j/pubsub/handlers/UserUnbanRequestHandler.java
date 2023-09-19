package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.UpdatedUnbanRequest;
import com.github.twitch4j.pubsub.events.UserUnbanRequestUpdateEvent;

import java.util.Collection;

class UserUnbanRequestHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "user-unban-requests";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if (topicParts.length == 3 && "update_unban_request".equals(message.getType())) {
            String userId = topicParts[1];
            String channelId = topicParts[2];
            UpdatedUnbanRequest request = TypeConvert.convertValue(message.getMessageData(), UpdatedUnbanRequest.class);
            eventManager.publish(new UserUnbanRequestUpdateEvent(userId, channelId, request));
            return true;
        }
        return false;
    }
}
