package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class RedemptionMetadata {

    @Nullable
    private Animation sendAnimatedMessageMetadata;

    @Nullable
    private Gigantification sendGigantifiedEmoteMetadata;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Animation {
        private String animationId;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Gigantification {
        private Emote emote;
    }

}
