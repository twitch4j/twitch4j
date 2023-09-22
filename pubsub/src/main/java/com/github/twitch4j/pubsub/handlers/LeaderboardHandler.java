package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.domain.Leaderboard;
import com.github.twitch4j.pubsub.events.BitsLeaderboardEvent;
import com.github.twitch4j.pubsub.events.SubLeaderboardEvent;

class LeaderboardHandler implements TopicHandler {
    @Override
    public String topicName() {
        return "leaderboard-events-v1";
    }

    @Override
    public boolean handle(Args args) {
        Leaderboard leaderboard = TypeConvert.jsonToObject(args.getRawMessage(), Leaderboard.class);
        switch (leaderboard.getIdentifier().getDomain()) {
            case "bits-usage-by-channel-v1":
                args.getEventManager().publish(new BitsLeaderboardEvent(leaderboard));
                return true;
            case "sub-gifts-sent":
                args.getEventManager().publish(new SubLeaderboardEvent(leaderboard));
                return true;
            default:
                return false;
        }
    }
}
