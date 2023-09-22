package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.ChannelTermsAction;
import com.github.twitch4j.pubsub.domain.ChatModerationAction;
import com.github.twitch4j.pubsub.domain.ModeratorUnbanRequestAction;
import com.github.twitch4j.pubsub.events.ChannelTermsEvent;
import com.github.twitch4j.pubsub.events.ChatModerationEvent;
import com.github.twitch4j.pubsub.events.ModUnbanRequestActionEvent;

import java.util.Collections;

class ModeratorHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "chat_moderator_actions";
    }

    @Override
    public boolean handle(Args args) {
        String type = args.getType();
        JsonNode msgData = args.getData();
        String channelId = args.getLastTopicPart();
        IEventManager eventManager = args.getEventManager();
        switch (type) {
            case "moderation_action":
                ChatModerationAction modAction = TypeConvert.convertValue(msgData, ChatModerationAction.class);
                eventManager.publish(new ChatModerationEvent(channelId, modAction));
                return true;

            case "channel_terms_action":
                ChannelTermsAction termsAction = TypeConvert.convertValue(msgData, ChannelTermsAction.class);
                eventManager.publish(new ChannelTermsEvent(channelId, termsAction));
                return true;

            case "approve_unban_request":
            case "deny_unban_request":
                ModeratorUnbanRequestAction unbanRequestAction = TypeConvert.convertValue(msgData, ModeratorUnbanRequestAction.class);
                eventManager.publish(new ModUnbanRequestActionEvent(channelId, unbanRequestAction));
                return true;

            case "moderator_added":
            case "moderator_removed":
            case "vip_added":
            case "vip_removed":
                ChatModerationAction.ModerationAction act = "moderator_added".equals(type) ? ChatModerationAction.ModerationAction.MOD
                    : "moderator_removed".equals(type) ? ChatModerationAction.ModerationAction.UNMOD
                    : "vip_added".equals(type) ? ChatModerationAction.ModerationAction.VIP
                    : ChatModerationAction.ModerationAction.UNVIP;

                String targetUserId = msgData.path("target_user_id").asText();
                String targetUserName = msgData.path("target_user_login").asText();
                String createdByUserId = msgData.path("created_by_user_id").asText();
                String createdBy = msgData.path("created_by").asText();
                ChatModerationAction action = new ChatModerationAction("chat_login_moderation", act, Collections.singletonList(targetUserName), createdBy, createdByUserId, "", targetUserId, targetUserName, false);
                eventManager.publish(new ChatModerationEvent(channelId, action));
                return true;

            default:
                return false;
        }
    }
}
