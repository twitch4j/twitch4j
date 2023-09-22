package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.events.ChatHighlightEvent;

class HighlightHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-chat-highlights";
    }

    @Override
    public boolean handle(Args args) {
        if ("chat-highlight".equals(args.getType())) {
            args.getEventManager().publish(TypeConvert.convertValue(args.getData(), ChatHighlightEvent.class));
            return true;
        }
        return false;
    }
}
