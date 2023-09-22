package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.AutomodCaughtMessageData;
import com.github.twitch4j.pubsub.events.AutomodCaughtMessageEvent;

class AutoModQueueHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "automod-queue";
    }

    @Override
    public boolean handle(Args args) {
        String[] topicParts = args.getTopicParts();
        if (topicParts.length == 3 && "automod_caught_message".equalsIgnoreCase(args.getType())) {
            AutomodCaughtMessageData data = TypeConvert.convertValue(args.getData(), AutomodCaughtMessageData.class);
            args.getEventManager().publish(new AutomodCaughtMessageEvent(topicParts[2], data));
            return true;
        }
        return false;
    }
}
