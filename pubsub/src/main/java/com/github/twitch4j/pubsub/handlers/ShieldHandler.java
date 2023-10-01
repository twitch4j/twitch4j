package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.BannedTermAdded;
import com.github.twitch4j.pubsub.domain.BannedTermRemoved;
import com.github.twitch4j.pubsub.domain.ShieldModeSettings;
import com.github.twitch4j.pubsub.domain.ShieldModeStatus;
import com.github.twitch4j.pubsub.events.ShieldModeBannedTermAddedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeBannedTermRemovedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeSettingsUpdatedEvent;
import com.github.twitch4j.pubsub.events.ShieldModeStatusUpdatedEvent;

class ShieldHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "shield-mode";
    }

    @Override
    public TwitchEvent apply(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length != 3) return null;
        String userId = topicParts[1];
        String channelId = topicParts[2];
        JsonNode msgData = args.getData();
        switch (args.getType()) {
            case "ADD_AUTOBAN_TERM":
                BannedTermAdded termAdded = TypeConvert.convertValue(msgData, BannedTermAdded.class);
                return new ShieldModeBannedTermAddedEvent(userId, channelId, termAdded);

            case "REMOVE_AUTOBAN_TERM":
                BannedTermRemoved termRemoved = TypeConvert.convertValue(msgData, BannedTermRemoved.class);
                return new ShieldModeBannedTermRemovedEvent(userId, channelId, termRemoved);

            case "UPDATE_CHANNEL_MODERATION_MODE":
                ShieldModeStatus shieldModeStatus = TypeConvert.convertValue(msgData, ShieldModeStatus.class);
                return new ShieldModeStatusUpdatedEvent(userId, channelId, shieldModeStatus);

            case "UPDATE_CHANNEL_MODERATION_SETTINGS":
                ShieldModeSettings shieldModeSettings = TypeConvert.convertValue(msgData, ShieldModeSettings.class);
                return new ShieldModeSettingsUpdatedEvent(userId, channelId, shieldModeSettings);

            case "UPDATE_CHANNEL_MODERATION_MODE_SHORTCUT":
                // do nothing; is_shortcut_enabled is unimportant
                return new TwitchEvent() {};

            default:
                return null;
        }
    }
}
