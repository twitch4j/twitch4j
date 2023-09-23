package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.SubGiftData;
import com.github.twitch4j.pubsub.events.ChannelSubGiftEvent;

class GiftHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-sub-gifts-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        return new ChannelSubGiftEvent(TypeConvert.jsonToObject(args.getRawMessage(), SubGiftData.class));
    }
}
