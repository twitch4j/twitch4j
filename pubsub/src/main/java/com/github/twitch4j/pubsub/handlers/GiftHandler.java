package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.SubGiftData;
import com.github.twitch4j.pubsub.events.ChannelSubGiftEvent;

class GiftHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-sub-gifts-v1";
    }

    @Override
    public boolean handle(Args args) {
        args.getEventManager().publish(new ChannelSubGiftEvent(TypeConvert.jsonToObject(args.getRawMessage(), SubGiftData.class)));
        return true;
    }
}
