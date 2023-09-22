package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.FollowingData;
import com.github.twitch4j.pubsub.events.FollowingEvent;

class FollowingHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "following";
    }

    @Override
    public boolean handle(Args args) {
        FollowingData data = TypeConvert.jsonToObject(args.getRawMessage(), FollowingData.class);
        args.getEventManager().publish(new FollowingEvent(args.getLastTopicPart(), data));
        return true;
    }
}
