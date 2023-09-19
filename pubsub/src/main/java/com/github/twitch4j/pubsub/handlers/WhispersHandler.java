package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import com.github.twitch4j.common.util.TwitchUtils;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.WhisperThread;
import com.github.twitch4j.pubsub.events.WhisperThreadUpdateEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class WhispersHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "whispers";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        // Whisper data is escaped Json cast into a String
        JsonNode msgDataParsed = TypeConvert.jsonToObject(message.getMessageData().asText(), JsonNode.class);
        String type = message.getType();
        if ("whisper_sent".equals(type) || "whisper_received".equals(type)) {
            //TypeReference<T> allows type parameters (unlike Class<T>) and avoids needing @SuppressWarnings("unchecked")
            Map<String, Object> tags = TypeConvert.convertValue(msgDataParsed.path("tags"), new TypeReference<Map<String, Object>>() {});

            String fromId = msgDataParsed.get("from_id").asText();
            String displayName = (String) tags.get("display_name");
            EventUser eventUser = new EventUser(fromId, displayName);

            String body = msgDataParsed.get("body").asText();

            Set<CommandPermission> permissions = TwitchUtils.getPermissionsFromTags(tags, new HashMap<>(), fromId, botOwnerIds);

            PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(eventUser, body, permissions);
            eventManager.publish(privateMessageEvent);
            return true;
        }
        if ("thread".equals(type)) {
            WhisperThread thread = TypeConvert.convertValue(msgDataParsed, WhisperThread.class);
            eventManager.publish(new WhisperThreadUpdateEvent(topicParts[topicParts.length - 1], thread));
            return true;
        }
        return false;
    }
}
