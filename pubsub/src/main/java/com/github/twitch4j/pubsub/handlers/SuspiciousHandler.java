package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.BanSharingSettings;
import com.github.twitch4j.pubsub.domain.LowTrustUserNewMessage;
import com.github.twitch4j.pubsub.domain.LowTrustUserTreatmentUpdate;
import com.github.twitch4j.pubsub.events.BanSharingSettingsUpdateEvent;
import com.github.twitch4j.pubsub.events.LowTrustUserNewMessageEvent;
import com.github.twitch4j.pubsub.events.LowTrustUserTreatmentUpdateEvent;

import java.util.Collection;

class SuspiciousHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "low-trust-users";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        if (topicParts.length != 3) return false;
        String userId = topicParts[1];
        String channelId = topicParts[2];
        String type = message.getType();
        JsonNode msgData = message.getMessageData();
        if ("low_trust_user_new_message".equals(type)) {
            eventManager.publish(new LowTrustUserNewMessageEvent(userId, channelId, TypeConvert.convertValue(msgData, LowTrustUserNewMessage.class)));
            return true;
        } else if ("low_trust_user_treatment_update".equals(type)) {
            eventManager.publish(new LowTrustUserTreatmentUpdateEvent(userId, channelId, TypeConvert.convertValue(msgData, LowTrustUserTreatmentUpdate.class)));
            return true;
        } else if ("bans_sharing_settings_update".equals(type)) {
            eventManager.publish(new BanSharingSettingsUpdateEvent(userId, channelId, TypeConvert.convertValue(msgData, BanSharingSettings.class)));
            return true;
        }
        return false;
    }
}
