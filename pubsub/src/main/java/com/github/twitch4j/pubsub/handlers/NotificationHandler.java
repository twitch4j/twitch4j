package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.CreateNotificationData;
import com.github.twitch4j.pubsub.domain.UpdateSummaryData;
import com.github.twitch4j.pubsub.events.OnsiteNotificationCreationEvent;
import com.github.twitch4j.pubsub.events.UpdateOnsiteNotificationSummaryEvent;

import java.util.Collection;

class NotificationHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "onsite-notifications";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if ("create-notification".equalsIgnoreCase(message.getType())) {
            CreateNotificationData data = TypeConvert.convertValue(message.getMessageData(), CreateNotificationData.class);
            eventManager.publish(new OnsiteNotificationCreationEvent(data));
            return true;
        } else if ("update-summary".equalsIgnoreCase(message.getType())) {
            UpdateSummaryData data = TypeConvert.convertValue(message.getMessageData(), UpdateSummaryData.class);
            eventManager.publish(new UpdateOnsiteNotificationSummaryEvent(topicParts[topicParts.length - 1], data));
            return true;
        }
        return false;
    }
}
