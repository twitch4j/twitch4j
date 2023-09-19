package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.CreatorGoal;
import com.github.twitch4j.pubsub.events.CreatorGoalEvent;

import java.util.Collection;

class GoalsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "creator-goals-events-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        CreatorGoal creatorGoal = TypeConvert.convertValue(message.getMessageData().path("goal"), CreatorGoal.class);
        eventManager.publish(new CreatorGoalEvent(topicParts[topicParts.length - 1], message.getType(), creatorGoal));
        return true;
    }
}
