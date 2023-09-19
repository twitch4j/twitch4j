package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.CreatedUnbanRequest;
import com.github.twitch4j.pubsub.domain.UpdatedUnbanRequest;
import com.github.twitch4j.pubsub.events.ChannelUnbanRequestCreateEvent;
import com.github.twitch4j.pubsub.events.ChannelUnbanRequestUpdateEvent;

import java.util.Collection;

class UnbanRequestHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-unban-requests";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if (topicParts.length != 3) return false;
        String userId = topicParts[1];
        String channelId = topicParts[2];
        if ("create_unban_request".equals(message.getType())) {
            CreatedUnbanRequest request = TypeConvert.convertValue(message.getMessageData(), CreatedUnbanRequest.class);
            eventManager.publish(new ChannelUnbanRequestCreateEvent(userId, channelId, request));
            return true;
        } else if ("update_unban_request".equals(message.getType())) {
            UpdatedUnbanRequest request = TypeConvert.convertValue(message.getMessageData(), UpdatedUnbanRequest.class);
            eventManager.publish(new ChannelUnbanRequestUpdateEvent(userId, channelId, request));
            return true;
        }
        return false;
    }
}
