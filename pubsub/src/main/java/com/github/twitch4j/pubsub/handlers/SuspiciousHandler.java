package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.BanSharingSettings;
import com.github.twitch4j.pubsub.domain.LowTrustUserNewMessage;
import com.github.twitch4j.pubsub.domain.LowTrustUserTreatmentUpdate;
import com.github.twitch4j.pubsub.events.BanSharingSettingsUpdateEvent;
import com.github.twitch4j.pubsub.events.LowTrustUserNewMessageEvent;
import com.github.twitch4j.pubsub.events.LowTrustUserTreatmentUpdateEvent;

class SuspiciousHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "low-trust-users";
    }

    @Override
    public TwitchEvent apply(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length != 3) return null;
        String userId = topicParts[1];
        String channelId = topicParts[2];
        String type = args.getType();
        JsonNode msgData = args.getData();
        if ("low_trust_user_new_message".equals(type)) {
            return new LowTrustUserNewMessageEvent(userId, channelId, TypeConvert.convertValue(msgData, LowTrustUserNewMessage.class));
        } else if ("low_trust_user_treatment_update".equals(type)) {
            return new LowTrustUserTreatmentUpdateEvent(userId, channelId, TypeConvert.convertValue(msgData, LowTrustUserTreatmentUpdate.class));
        } else if ("bans_sharing_settings_update".equals(type)) {
            return new BanSharingSettingsUpdateEvent(userId, channelId, TypeConvert.convertValue(msgData, BanSharingSettings.class));
        }
        return null;
    }
}
