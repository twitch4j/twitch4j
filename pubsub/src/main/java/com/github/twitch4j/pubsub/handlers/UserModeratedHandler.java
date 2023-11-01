package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.UserAutomodCaughtMessage;
import com.github.twitch4j.pubsub.events.UserAutomodCaughtMessageEvent;

class UserModeratedHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "user-moderation-notifications";
    }

    @Override
    public TwitchEvent apply(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length == 3 && "automod_caught_message".equalsIgnoreCase(args.getType())) {
            UserAutomodCaughtMessage data = TypeConvert.convertValue(args.getData(), UserAutomodCaughtMessage.class);
            return new UserAutomodCaughtMessageEvent(topicParts[1], topicParts[2], data);
        }
        return null;
    }
}
