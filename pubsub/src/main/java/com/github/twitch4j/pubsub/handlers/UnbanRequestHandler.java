package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.CreatedUnbanRequest;
import com.github.twitch4j.pubsub.domain.UpdatedUnbanRequest;
import com.github.twitch4j.pubsub.events.ChannelUnbanRequestCreateEvent;
import com.github.twitch4j.pubsub.events.ChannelUnbanRequestUpdateEvent;

class UnbanRequestHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-unban-requests";
    }

    @Override
    public boolean handle(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length != 3) return false;
        String userId = topicParts[1];
        String channelId = topicParts[2];
        if ("create_unban_request".equals(args.getType())) {
            CreatedUnbanRequest request = TypeConvert.convertValue(args.getData(), CreatedUnbanRequest.class);
            args.getEventManager().publish(new ChannelUnbanRequestCreateEvent(userId, channelId, request));
            return true;
        } else if ("update_unban_request".equals(args.getType())) {
            UpdatedUnbanRequest request = TypeConvert.convertValue(args.getData(), UpdatedUnbanRequest.class);
            args.getEventManager().publish(new ChannelUnbanRequestUpdateEvent(userId, channelId, request));
            return true;
        }
        return false;
    }
}
