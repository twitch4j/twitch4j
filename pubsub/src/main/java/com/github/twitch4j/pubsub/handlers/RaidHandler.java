package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
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
    public TwitchEvent apply(Args args) {
        switch (args.getType()) {
            case "raid_go_v2":
                return TypeConvert.jsonToObject(args.getRawMessage(), RaidGoEvent.class);
            case "raid_update_v2":
                return TypeConvert.jsonToObject(args.getRawMessage(), RaidUpdateEvent.class);
            case "raid_cancel_v2":
                return TypeConvert.jsonToObject(args.getRawMessage(), RaidCancelEvent.class);
            default:
                return null;
        }
    }
}
