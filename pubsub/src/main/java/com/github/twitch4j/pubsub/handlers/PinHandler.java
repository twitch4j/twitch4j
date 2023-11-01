package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.DeletePinnedChatData;
import com.github.twitch4j.pubsub.domain.PinnedChatData;
import com.github.twitch4j.pubsub.domain.UpdatedPinnedChatTiming;
import com.github.twitch4j.pubsub.events.PinnedChatCreatedEvent;
import com.github.twitch4j.pubsub.events.PinnedChatDeletedEvent;
import com.github.twitch4j.pubsub.events.PinnedChatTimingUpdatedEvent;

class PinHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "pinned-chat-updates-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        JsonNode msgData = args.getData();
        switch (args.getType()) {
            case "pin-message":
                PinnedChatData createdPin = TypeConvert.convertValue(msgData, PinnedChatData.class);
                return new PinnedChatCreatedEvent(args.getLastTopicPart(), createdPin);
            case "update-message":
                UpdatedPinnedChatTiming updatedPin = TypeConvert.convertValue(msgData, UpdatedPinnedChatTiming.class);
                return new PinnedChatTimingUpdatedEvent(args.getLastTopicPart(), updatedPin);
            case "unpin-message":
                DeletePinnedChatData deletePin = TypeConvert.convertValue(msgData, DeletePinnedChatData.class);
                return new PinnedChatDeletedEvent(args.getLastTopicPart(), deletePin);
            default:
                return null;
        }
    }
}
