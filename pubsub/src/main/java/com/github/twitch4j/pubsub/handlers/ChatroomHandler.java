package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.AliasRestrictionUpdateData;
import com.github.twitch4j.pubsub.domain.UserModerationActionData;
import com.github.twitch4j.pubsub.events.AliasRestrictionUpdateEvent;
import com.github.twitch4j.pubsub.events.UserModerationActionEvent;

class ChatroomHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "chatrooms-user-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length <= 1) return null;
        String userId = topicParts[1];
        JsonNode msgData = args.getData();
        switch (args.getType()) {
            case "channel_banned_alias_restriction_update":
                final AliasRestrictionUpdateData aliasData = TypeConvert.convertValue(msgData, AliasRestrictionUpdateData.class);
                return new AliasRestrictionUpdateEvent(userId, aliasData);

            case "user_moderation_action":
                final UserModerationActionData actionData = TypeConvert.convertValue(msgData, UserModerationActionData.class);
                return new UserModerationActionEvent(userId, actionData);

            default:
                return null;
        }
    }
}
