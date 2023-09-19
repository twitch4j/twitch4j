package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.PollData;
import com.github.twitch4j.pubsub.events.PollsEvent;

import java.util.Collection;

class PollsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "polls";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        PollData pollData = TypeConvert.convertValue(message.getMessageData().path("poll"), PollData.class);
        eventManager.publish(new PollsEvent(message.getType(), pollData));
        return true;
    }
}
