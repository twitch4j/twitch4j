package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
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
    public TwitchEvent apply(Args args) {
        if ("create-notification".equalsIgnoreCase(args.getType())) {
            CreateNotificationData data = TypeConvert.convertValue(args.getData(), CreateNotificationData.class);
            return new OnsiteNotificationCreationEvent(data);
        } else if ("update-summary".equalsIgnoreCase(args.getType())) {
            UpdateSummaryData data = TypeConvert.convertValue(args.getData(), UpdateSummaryData.class);
            return new UpdateOnsiteNotificationSummaryEvent(args.getLastTopicPart(), data);
        }
        return null;
    }
}
