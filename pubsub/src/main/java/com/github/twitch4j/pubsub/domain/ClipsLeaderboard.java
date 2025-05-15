package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ClipsLeaderboard {
    private String type; // e.g., "clips-leaderboard-update"
    private String broadcasterId;
    private String timeUnit; // e.g., "DAY"
    private Instant endTime;
    private List<Entry> newLeaderboard;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Entry {
        private int rank;
        private String curatorId;
        private String curatorDisplayName;
        private String curatorLogin;
        private String clipId;
        private String clipSlug;
        private String clipAssetId;
        private String clipTitle;
        private String clipThumbnailUrl;
        private String clipUrl;
        private int score;
    }
}
