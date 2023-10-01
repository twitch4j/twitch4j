package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.events.PredictionCreatedEvent;
import com.github.twitch4j.pubsub.events.PredictionUpdatedEvent;

class PredictionHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "predictions-channel-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        if ("event-created".equals(args.getType())) {
            return TypeConvert.convertValue(args.getData(), PredictionCreatedEvent.class);
        } else if ("event-updated".equals(args.getType())) {
            return TypeConvert.convertValue(args.getData(), PredictionUpdatedEvent.class);
        }
        return null;
    }
}
