package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.MidrollRequest;
import com.github.twitch4j.pubsub.events.MidrollRequestEvent;

class AdsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "ads";
    }

    @Override
    public TwitchEvent apply(Args args) {
        if ("midroll_request".equals(args.getType())) {
            MidrollRequest midroll = TypeConvert.convertValue(args.getData(), MidrollRequest.class);
            return new MidrollRequestEvent(args.getLastTopicPart(), midroll);
        }
        return null;
    }
}
