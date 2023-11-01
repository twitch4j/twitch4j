package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.CreateShoutoutData;
import com.github.twitch4j.pubsub.events.ShoutoutCreatedEvent;

class ShoutoutHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "shoutout";
    }

    @Override
    public TwitchEvent apply(Args args) {
        if ("create".equalsIgnoreCase(args.getType())) {
            CreateShoutoutData shoutoutInfo = TypeConvert.convertValue(args.getData(), CreateShoutoutData.class);
            return new ShoutoutCreatedEvent(args.getLastTopicPart(), shoutoutInfo);
        }
        return null;
    }
}
