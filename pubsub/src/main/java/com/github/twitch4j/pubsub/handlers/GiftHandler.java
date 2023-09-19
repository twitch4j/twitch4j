package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.SubGiftData;
import com.github.twitch4j.pubsub.events.ChannelSubGiftEvent;

import java.util.Collection;

class GiftHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-sub-gifts-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        eventManager.publish(new ChannelSubGiftEvent(TypeConvert.jsonToObject(message.getRawMessage(), SubGiftData.class)));
        return true;
    }
}
