package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.AliasRestrictionUpdateData;
import com.github.twitch4j.pubsub.domain.UserModerationActionData;
import com.github.twitch4j.pubsub.events.AliasRestrictionUpdateEvent;
import com.github.twitch4j.pubsub.events.UserModerationActionEvent;

import java.util.Collection;

class ChatroomHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "chatrooms-user-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if (topicParts.length <= 1) return false;
        String userId = topicParts[1];
        JsonNode msgData = message.getMessageData();
        switch (message.getType()) {
            case "channel_banned_alias_restriction_update":
                final AliasRestrictionUpdateData aliasData = TypeConvert.convertValue(msgData, AliasRestrictionUpdateData.class);
                eventManager.publish(new AliasRestrictionUpdateEvent(userId, aliasData));
                return true;

            case "user_moderation_action":
                final UserModerationActionData actionData = TypeConvert.convertValue(msgData, UserModerationActionData.class);
                eventManager.publish(new UserModerationActionEvent(userId, actionData));
                return true;

            default:
                return false;
        }
    }
}
