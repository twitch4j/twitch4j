package com.github.twitch4j.eventsub.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.twitch4j.eventsub.EventSubNotification;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes;
import com.github.twitch4j.helix.domain.EventSubSubscription;

import java.io.IOException;
import java.util.Optional;

public class NotificationDeserializer extends JsonDeserializer<EventSubNotification> {

    @Override
    public EventSubNotification deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final JsonNode root = p.getCodec().readTree(p);
        if (root == null) return null;

        final EventSubSubscription sub = getObject(p.getCodec(), root, "subscription", EventSubSubscription.class);
        if (sub == null) return null;

        final SubscriptionType<?, ?, ?> type = SubscriptionTypes.getSubscriptionType(sub.getType(), sub.getVersion());

        final EventSubCondition condition;
        final EventSubEvent event;
        if (type != null) {
            condition = EventSubConditionConverter.getCondition(type, sub.getCondition());
            event = getObject(p.getCodec(), root, "event", type.getEventClass());
        } else {
            condition = null;
            event = null;
        }

        return new EventSubNotification(sub, type, condition, event);
    }

    private static <T> T getObject(ObjectCodec codec, JsonNode parent, String field, Class<T> clazz) {
        return Optional.ofNullable(parent)
            .map(r -> r.get(field))
            .filter(JsonNode::isObject)
            .map(node -> {
                try {
                    return codec.treeToValue(node, clazz);
                } catch (JsonProcessingException e) {
                    return null;
                }
            })
            .orElse(null);
    }

}
