package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.ScheduleUpdate;
import com.github.twitch4j.pubsub.events.AdsScheduleUpdateEvent;

class AdsManagerHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "ads-manager";
    }

    @Override
    public boolean handle(Args args) {
        if ("ads-schedule-update".equals(args.getType())) {
            ScheduleUpdate data = TypeConvert.jsonToObject(args.getRawMessage(), ScheduleUpdate.class);
            args.getEventManager().publish(new AdsScheduleUpdateEvent(args.getLastTopicPart(), data));
            return true;
        }
        return false;
    }
}
