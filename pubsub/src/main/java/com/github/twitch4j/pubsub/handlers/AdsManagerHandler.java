package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.ScheduleUpdate;
import com.github.twitch4j.pubsub.events.AdsScheduleUpdateEvent;

import java.util.Collection;

class AdsManagerHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "ads-manager";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("ads-schedule-update".equals(message.getType())) {
            ScheduleUpdate data = TypeConvert.jsonToObject(message.getRawMessage(), ScheduleUpdate.class);
            eventManager.publish(new AdsScheduleUpdateEvent(topicParts[topicParts.length - 1], data));
            return true;
        }
        return false;
    }
}
