package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.CommunityBoostProgression;
import com.github.twitch4j.pubsub.events.CommunityBoostProgressionEvent;

import java.util.Collection;

class BoostHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "community-boost-events-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("community-boost-progression".equals(message.getType())) {
            CommunityBoostProgression progression = TypeConvert.convertValue(message.getMessageData(), CommunityBoostProgression.class);
            eventManager.publish(new CommunityBoostProgressionEvent(progression));
            return true;
        }
        return false;
    }
}
