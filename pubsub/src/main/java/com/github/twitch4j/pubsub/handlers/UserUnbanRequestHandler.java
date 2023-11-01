package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.UpdatedUnbanRequest;
import com.github.twitch4j.pubsub.events.UserUnbanRequestUpdateEvent;

class UserUnbanRequestHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "user-unban-requests";
    }

    @Override
    public TwitchEvent apply(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length == 3 && "update_unban_request".equals(args.getType())) {
            String userId = topicParts[1];
            String channelId = topicParts[2];
            UpdatedUnbanRequest request = TypeConvert.convertValue(args.getData(), UpdatedUnbanRequest.class);
            return new UserUnbanRequestUpdateEvent(userId, channelId, request);
        }
        return null;
    }
}
