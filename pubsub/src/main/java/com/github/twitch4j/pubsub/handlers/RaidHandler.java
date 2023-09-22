package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.events.RaidCancelEvent;
import com.github.twitch4j.pubsub.events.RaidGoEvent;
import com.github.twitch4j.pubsub.events.RaidUpdateEvent;

class RaidHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "raid";
    }

    @Override
    public boolean handle(Args args) {
        IEventManager eventManager = args.getEventManager();
        switch (args.getType()) {
            case "raid_go_v2":
                eventManager.publish(TypeConvert.jsonToObject(args.getRawMessage(), RaidGoEvent.class));
                return true;
            case "raid_update_v2":
                eventManager.publish(TypeConvert.jsonToObject(args.getRawMessage(), RaidUpdateEvent.class));
                return true;
            case "raid_cancel_v2":
                eventManager.publish(TypeConvert.jsonToObject(args.getRawMessage(), RaidCancelEvent.class));
                return true;
            default:
                return false;
        }
    }
}
