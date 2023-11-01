package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.AutomodCaughtMessageData;
import com.github.twitch4j.pubsub.events.AutomodCaughtMessageEvent;

class AutoModQueueHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "automod-queue";
    }

    @Override
    public TwitchEvent apply(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length == 3 && "automod_caught_message".equalsIgnoreCase(args.getType())) {
            AutomodCaughtMessageData data = TypeConvert.convertValue(args.getData(), AutomodCaughtMessageData.class);
            return new AutomodCaughtMessageEvent(topicParts[2], data);
        }
        return null;
    }
}
