package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(onMethod_ = { @Deprecated })
public class HypeTrainReward {
    private String type; // e.g. "EMOTE"
    private String id;
    private @Nullable Emote emote;
    private @Nullable String groupId;
    private @Nullable Integer rewardLevel;
    private @Nullable String setId;
    private @Nullable String token;
    private @Nullable Instant rewardEndDate; // 0001-01-01T00:00:00Z corresponds to forever

    public boolean isTemporary() {
        return rewardEndDate != null && rewardEndDate.isAfter(Instant.EPOCH);
    }
}
