package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.BannedTermAdded;
import com.github.twitch4j.pubsub.domain.BannedTermRemoved;
import com.github.twitch4j.pubsub.domain.ShieldModeSettings;
import com.github.twitch4j.pubsub.domain.ShieldModeStatus;
import com.github.twitch4j.pubsub.events.ShieldModeBannedTermAddedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeBannedTermRemovedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeSettingsUpdatedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeStatusUpdatedEvent;

import java.util.Collection;

class ShieldHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "shield-mode";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if (topicParts.length != 3) return false;
        String userId = topicParts[1];
        String channelId = topicParts[2];
        JsonNode msgData = message.getMessageData();
        switch (message.getType()) {
            case "ADD_AUTOBAN_TERM":
                BannedTermAdded termAdded = TypeConvert.convertValue(msgData, BannedTermAdded.class);
                eventManager.publish(new ShieldModeBannedTermAddedEvent(userId, channelId, termAdded));
                return true;

            case "REMOVE_AUTOBAN_TERM":
                BannedTermRemoved termRemoved = TypeConvert.convertValue(msgData, BannedTermRemoved.class);
                eventManager.publish(new ShieldModeBannedTermRemovedEvent(userId, channelId, termRemoved));
                return true;

            case "UPDATE_CHANNEL_MODERATION_MODE":
                ShieldModeStatus shieldModeStatus = TypeConvert.convertValue(msgData, ShieldModeStatus.class);
                eventManager.publish(new ShieldModeStatusUpdatedEvent(userId, channelId, shieldModeStatus));
                return true;

            case "UPDATE_CHANNEL_MODERATION_SETTINGS":
                ShieldModeSettings shieldModeSettings = TypeConvert.convertValue(msgData, ShieldModeSettings.class);
                eventManager.publish(new ShieldModeSettingsUpdatedEvent(userId, channelId, shieldModeSettings));
                return true;

            case "UPDATE_CHANNEL_MODERATION_MODE_SHORTCUT":
                // do nothing; is_shortcut_enabled is unimportant
                return true;

            default:
                return false;
        }
    }
}
