package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.events.ChatHighlightEvent;

class HighlightHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "channel-chat-highlights";
    }

    @Override
    public TwitchEvent apply(Args args) {
        if ("chat-highlight".equals(args.getType())) {
            return TypeConvert.convertValue(args.getData(), ChatHighlightEvent.class);
        }
        return null;
    }
}
