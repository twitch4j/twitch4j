package com.github.twitch4j.pubsub.handlers;

import com.github.twitch4j.common.events.TwitchEvent;
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
    public TwitchEvent apply(Args args) {
        Leaderboard leaderboard = TypeConvert.jsonToObject(args.getRawMessage(), Leaderboard.class);
        switch (leaderboard.getIdentifier().getDomain()) {
            case "bits-usage-by-channel-v1":
                return new BitsLeaderboardEvent(leaderboard);
            case "sub-gifts-sent":
                return new SubLeaderboardEvent(leaderboard);
            default:
                return null;
        }
    }
}
