package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.CommunityBoostProgression;
import com.github.twitch4j.pubsub.events.CommunityBoostProgressionEvent;

class BoostHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "community-boost-events-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        if ("community-boost-progression".equals(args.getType())) {
            CommunityBoostProgression progression = TypeConvert.convertValue(args.getData(), CommunityBoostProgression.class);
            return new CommunityBoostProgressionEvent(progression);
        }
        return null;
    }
}
