package com.github.twitch4j.chat.flag;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a region of a chat message that was flagged by AutoMod.
 */
@Value
@Builder
@Unofficial
public class AutoModFlag {
    /**
     * The index in the message where the flagged item starts.
     */
    int startIndex;

    /**
     * The index where the flagged item ends.
     */
    int endIndex;

    /**
     * Scores for the various {@link FlagType}s for this region of the message.
     * <p>
     * Can be empty, for example, if the region is a HTTP(S) link.
     */
    @NotNull
    @Singular
    Map<FlagType, Integer> scores;
}
