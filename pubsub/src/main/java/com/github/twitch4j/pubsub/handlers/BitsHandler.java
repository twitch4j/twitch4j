package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.ChannelBitsData;
import com.github.twitch4j.pubsub.events.ChannelBitsEvent;

class BitsHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-bits-events-v2";
    }

    @Override
    public TwitchEvent apply(Args args) {
        ChannelBitsData data = TypeConvert.convertValue(args.getData(), ChannelBitsData.class);
        return new ChannelBitsEvent(data);
    }
}
