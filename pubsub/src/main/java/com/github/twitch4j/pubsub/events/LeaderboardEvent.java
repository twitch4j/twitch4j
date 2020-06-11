package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.Leaderboard;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class LeaderboardEvent extends TwitchEvent {
    private final Leaderboard data;

    public String getChannelId() {
        return this.data.getIdentifier().getGroupingKey();
    }
}
