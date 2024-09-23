package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.SharedChatSession;
import com.github.twitch4j.pubsub.events.SharedChatSessionCreatedEvent;
import com.github.twitch4j.pubsub.events.SharedChatSessionEndedEvent;
import com.github.twitch4j.pubsub.events.SharedChatSessionStartedEvent;
import com.github.twitch4j.pubsub.events.SharedChatSessionUpdatedEvent;

import java.time.Instant;

class SharedChatHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "shared-chat-channel-v1";
    }

    @Override
    public TwitchEvent apply(Args args) {
        String channelId = args.getLastTopicPart();
        Instant ts = Instant.parse(args.getData().get("timestamp").textValue());
        SharedChatSession session = TypeConvert.convertValue(args.getData().path("session"), SharedChatSession.class);
        switch (args.getType()) {
            case "session-created":
                return new SharedChatSessionCreatedEvent(channelId, ts, session);
            case "session-started":
                return new SharedChatSessionStartedEvent(channelId, ts, session);
            case "session-updated":
                return new SharedChatSessionUpdatedEvent(channelId, ts, session);
            case "session-ended":
                return new SharedChatSessionEndedEvent(channelId, ts, session);
            default:
                return null;
        }
    }
}
