package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.FollowingData;
import com.github.twitch4j.pubsub.events.FollowingEvent;

class FollowingHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "following";
    }

    @Override
    public TwitchEvent apply(Args args) {
        FollowingData data = TypeConvert.jsonToObject(args.getRawMessage(), FollowingData.class);
        return new FollowingEvent(args.getLastTopicPart(), data);
    }
}
