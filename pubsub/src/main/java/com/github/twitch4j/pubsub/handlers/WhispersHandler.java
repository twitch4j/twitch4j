package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import com.github.twitch4j.common.util.TwitchUtils;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.WhisperThread;
import com.github.twitch4j.pubsub.events.WhisperThreadUpdateEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class WhispersHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "whispers";
    }

    @Override
    public TwitchEvent apply(Args args) {
        // Whisper data is escaped Json cast into a String
        JsonNode msgDataParsed = TypeConvert.jsonToObject(args.getData().asText(), JsonNode.class);
        String type = args.getType();
        if ("whisper_sent".equals(type) || "whisper_received".equals(type)) {
            //TypeReference<T> allows type parameters (unlike Class<T>) and avoids needing @SuppressWarnings("unchecked")
            Map<String, Object> tags = TypeConvert.convertValue(msgDataParsed.path("tags"), new TypeReference<Map<String, Object>>() {});

            String fromId = msgDataParsed.get("from_id").asText();
            String displayName = (String) tags.get("display_name");
            EventUser eventUser = new EventUser(fromId, displayName);

            String body = msgDataParsed.get("body").asText();

            Set<CommandPermission> permissions = TwitchUtils.getPermissionsFromTags(tags, new HashMap<>(), fromId, args.getBotOwnerIds());
            return new PrivateMessageEvent(eventUser, body, permissions);
        }
        if ("thread".equals(type)) {
            WhisperThread thread = TypeConvert.convertValue(msgDataParsed, WhisperThread.class);
            return new WhisperThreadUpdateEvent(args.getLastTopicPart(), thread);
        }
        return null;
    }
}
