package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.SubscriptionData;
import com.github.twitch4j.pubsub.events.ChannelSubscribeEvent;

class SubscriptionHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-subscribe-events-v1";
    }

    @Override
    public boolean handle(Args args) {
        SubscriptionData data = TypeConvert.jsonToObject(args.getRawMessage(), SubscriptionData.class);
        args.getEventManager().publish(new ChannelSubscribeEvent(data));
        return true;
    }
}
