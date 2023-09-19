package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.SubscriptionData;
import com.github.twitch4j.pubsub.events.ChannelSubscribeEvent;

import java.util.Collection;

class SubscriptionHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-subscribe-events-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        SubscriptionData data = TypeConvert.jsonToObject(message.getRawMessage(), SubscriptionData.class);
        eventManager.publish(new ChannelSubscribeEvent(data));
        return true;
    }
}
