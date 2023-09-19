package com.github.twitch4j.pubsub.handlers;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.PubSubResponsePayloadMessage;
import com.github.twitch4j.pubsub.domain.Leaderboard;
import com.github.twitch4j.pubsub.events.BitsLeaderboardEvent;
import com.github.twitch4j.pubsub.events.SubLeaderboardEvent;

import java.util.Collection;

class LeaderboardHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "leaderboard-events-v1";
    }

    @Override
    public boolean handle(IEventManager eventManager, String[] topicParts, PubSubResponsePayloadMessage message, Collection<String> botOwnerIds) {
        Leaderboard leaderboard = TypeConvert.jsonToObject(message.getRawMessage(), Leaderboard.class);
        switch (leaderboard.getIdentifier().getDomain()) {
            case "bits-usage-by-channel-v1":
                eventManager.publish(new BitsLeaderboardEvent(leaderboard));
                return true;
            case "sub-gifts-sent":
                eventManager.publish(new SubLeaderboardEvent(leaderboard));
                return true;
            default:
                return false;
        }
    }
}
