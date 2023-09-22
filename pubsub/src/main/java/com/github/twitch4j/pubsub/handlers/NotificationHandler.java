package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.CreateNotificationData;
import com.github.twitch4j.pubsub.domain.UpdateSummaryData;
import com.github.twitch4j.pubsub.events.OnsiteNotificationCreationEvent;
import com.github.twitch4j.pubsub.events.UpdateOnsiteNotificationSummaryEvent;

class NotificationHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "onsite-notifications";
    }

    @Override
    public boolean handle(Args args) {
        if ("create-notification".equalsIgnoreCase(args.getType())) {
            CreateNotificationData data = TypeConvert.convertValue(args.getData(), CreateNotificationData.class);
            args.getEventManager().publish(new OnsiteNotificationCreationEvent(data));
            return true;
        } else if ("update-summary".equalsIgnoreCase(args.getType())) {
            UpdateSummaryData data = TypeConvert.convertValue(args.getData(), UpdateSummaryData.class);
            args.getEventManager().publish(new UpdateOnsiteNotificationSummaryEvent(args.getLastTopicPart(), data));
            return true;
        }
        return false;
    }
}
