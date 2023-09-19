package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.DeletePinnedChatData;
import com.github.twitch4j.pubsub.domain.PinnedChatData;
import com.github.twitch4j.pubsub.domain.UpdatedPinnedChatTiming;
import com.github.twitch4j.pubsub.events.PinnedChatCreatedEvent;
import com.github.twitch4j.pubsub.events.PinnedChatDeletedEvent;
import com.github.twitch4j.pubsub.events.PinnedChatTimingUpdatedEvent;

import java.util.Collection;

class PinHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "pinned-chat-updates-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        JsonNode msgData = message.getMessageData();
        String lastTopicIdentifier = topicParts[topicParts.length - 1];
        switch (message.getType()) {
            case "pin-message":
                PinnedChatData createdPin = TypeConvert.convertValue(msgData, PinnedChatData.class);
                eventManager.publish(new PinnedChatCreatedEvent(lastTopicIdentifier, createdPin));
                return true;
            case "update-message":
                UpdatedPinnedChatTiming updatedPin = TypeConvert.convertValue(msgData, UpdatedPinnedChatTiming.class);
                eventManager.publish(new PinnedChatTimingUpdatedEvent(lastTopicIdentifier, updatedPin));
                return true;
            case "unpin-message":
                DeletePinnedChatData deletePin = TypeConvert.convertValue(msgData, DeletePinnedChatData.class);
                eventManager.publish(new PinnedChatDeletedEvent(lastTopicIdentifier, deletePin));
                return true;
            default:
                return false;
        }
    }
}
