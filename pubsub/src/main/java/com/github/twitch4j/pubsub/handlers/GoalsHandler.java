package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.CreatorGoal;
import com.github.twitch4j.pubsub.events.CreatorGoalEvent;

class GoalsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "creator-goals-events-v1";
    }

    @Override
    public boolean handle(Args args) {
        CreatorGoal creatorGoal = TypeConvert.convertValue(args.getData().path("goal"), CreatorGoal.class);
        args.getEventManager().publish(new CreatorGoalEvent(args.getLastTopicPart(), args.getType(), creatorGoal));
        return true;
    }
}
