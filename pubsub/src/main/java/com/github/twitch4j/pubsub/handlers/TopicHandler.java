package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;

import java.util.Collection;
import java.util.Collections;

public interface TopicHandler {

    String topicName();

    boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds);

    default Collection<String> topicNames() {
        return Collections.singletonList(topicName());
    }

}
