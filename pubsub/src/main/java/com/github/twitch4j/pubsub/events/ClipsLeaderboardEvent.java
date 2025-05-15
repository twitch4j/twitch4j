package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ClipsLeaderboard;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ClipsLeaderboardEvent extends TwitchEvent {
    ClipsLeaderboard leaderboard;

    public String getChannelId() {
        return leaderboard.getBroadcasterId();
    }
}
