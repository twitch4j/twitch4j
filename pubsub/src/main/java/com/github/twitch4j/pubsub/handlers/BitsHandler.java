package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.ChannelBitsData;
import com.github.twitch4j.pubsub.events.ChannelBitsEvent;

import java.util.Collection;

class BitsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-bits-events-v2";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        ChannelBitsData data = TypeConvert.convertValue(message.getMessageData(), ChannelBitsData.class);
        eventManager.publish(new ChannelBitsEvent(data));
        return true;
    }
}
