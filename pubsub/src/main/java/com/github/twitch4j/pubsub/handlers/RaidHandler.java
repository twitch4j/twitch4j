package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.events.RaidCancelEvent;
import com.github.twitch4j.pubsub.events.RaidGoEvent;
import com.github.twitch4j.pubsub.events.RaidUpdateEvent;

import java.util.Collection;

class RaidHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "raid";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        switch (message.getType()) {
            case "raid_go_v2":
                eventManager.publish(TypeConvert.jsonToObject(message.getRawMessage(), RaidGoEvent.class));
                return true;
            case "raid_update_v2":
                eventManager.publish(TypeConvert.jsonToObject(message.getRawMessage(), RaidUpdateEvent.class));
                return true;
            case "raid_cancel_v2":
                eventManager.publish(TypeConvert.jsonToObject(message.getRawMessage(), RaidCancelEvent.class));
                return true;
            default:
                return false;
        }
    }
}
