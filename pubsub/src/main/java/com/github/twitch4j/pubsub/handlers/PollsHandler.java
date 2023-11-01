package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.PollData;
import com.github.twitch4j.pubsub.events.PollsEvent;

class PollsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "polls";
    }

    @Override
    public TwitchEvent apply(Args args) {
        PollData pollData = TypeConvert.convertValue(args.getData().path("poll"), PollData.class);
        return new PollsEvent(args.getType(), pollData);
    }
}
