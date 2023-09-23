package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.CreatorGoal;
import com.github.twitch4j.pubsub.events.CreatorGoalEvent;

class GoalsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "creator-goals-events-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        CreatorGoal creatorGoal = TypeConvert.convertValue(args.getData().path("goal"), CreatorGoal.class);
        return new CreatorGoalEvent(args.getLastTopicPart(), args.getType(), creatorGoal);
    }
}
