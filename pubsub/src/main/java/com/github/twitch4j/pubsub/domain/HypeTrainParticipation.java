package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.OptionalInt;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeTrainParticipation {
    private String source;
    private String action;
    @JsonAlias("value")
    private int quantity;

    public boolean isCheer() {
        return "BITS".equals(source);
    }

    public boolean isSub() {
        return "SUBS".equals(source);
    }

    public boolean isGifted() {
        return action != null && action.endsWith("GIFTED_SUB");
    }

    public OptionalInt getSubTier() {
        if (action == null || !isSub()) return OptionalInt.empty();
        final int tier = action.charAt("TIER_".length()) - '0';
        return OptionalInt.of(tier);
    }
}
