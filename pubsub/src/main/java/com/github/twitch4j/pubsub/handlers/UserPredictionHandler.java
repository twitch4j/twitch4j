package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.events.UserPredictionMadeEvent;
import com.github.twitch4j.pubsub.events.UserPredictionResultEvent;

class UserPredictionHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "predictions-user-v1";
    }

    @Override
    public boolean handle(Args args) {
        if ("prediction-made".equals(args.getType())) {
            args.getEventManager().publish(TypeConvert.convertValue(args.getData(), UserPredictionMadeEvent.class));
            return true;
        } else if ("prediction-result".equals(args.getType())) {
            args.getEventManager().publish(TypeConvert.convertValue(args.getData(), UserPredictionResultEvent.class));
            return true;
        }
        return false;
    }
}
