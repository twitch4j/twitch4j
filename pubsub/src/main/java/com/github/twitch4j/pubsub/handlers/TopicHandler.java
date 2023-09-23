package com.github.twitch4j.pubsub.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import lombok.Value;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public interface TopicHandler extends Function<TopicHandler.Args, TwitchEvent> {

    String topicName();

    default Collection<String> topicNames() {
        return Collections.singletonList(topicName());
    }

    @Value
    class Args {
        String[] topicParts;
        PubSubResponsePayloadMessage message;
        Collection<String> botOwnerIds;

        public String getType() {
            return message.getType();
        }

        public JsonNode getData() {
            return message.getMessageData();
        }

        public String getRawMessage() {
            return message.getRawMessage();
        }

        public String getLastTopicPart() {
            return topicParts[topicParts.length - 1];
        }
    }
}
