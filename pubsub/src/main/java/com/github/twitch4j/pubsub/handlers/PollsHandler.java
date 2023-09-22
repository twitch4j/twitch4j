package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.PollData;
import com.github.twitch4j.pubsub.events.PollsEvent;

class PollsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "polls";
    }

    @Override
    public boolean handle(Args args) {
        PollData pollData = TypeConvert.convertValue(args.getData().path("poll"), PollData.class);
        args.getEventManager().publish(new PollsEvent(args.getType(), pollData));
        return true;
    }
}
