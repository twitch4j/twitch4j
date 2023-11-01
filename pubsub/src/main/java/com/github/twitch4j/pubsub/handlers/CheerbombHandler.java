package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.CheerbombData;
import com.github.twitch4j.pubsub.events.CheerbombEvent;

class CheerbombHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-cheer-events-public-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        if ("cheerbomb".equalsIgnoreCase(args.getType())) {
            CheerbombData cheerbomb = TypeConvert.convertValue(args.getData(), CheerbombData.class);
            return new CheerbombEvent(args.getLastTopicPart(), cheerbomb);
        }
        return null;
    }
}
