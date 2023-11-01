package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.BitsBadgeData;
import com.github.twitch4j.pubsub.events.ChannelBitsBadgeUnlockEvent;

class BitsBadgeHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-bits-badge-unlocks";
    }

    @Override
    public TwitchEvent apply(Args args) {
        BitsBadgeData data = TypeConvert.jsonToObject(args.getRawMessage(), BitsBadgeData.class);
        return new ChannelBitsBadgeUnlockEvent(data);
    }
}
