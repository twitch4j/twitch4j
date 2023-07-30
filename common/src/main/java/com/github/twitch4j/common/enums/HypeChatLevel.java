package com.github.twitch4j.common.enums;

import com.github.twitch4j.util.EnumUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.time.Duration;
import java.util.Map;

/**
 * @see <a href="https://help.twitch.tv/s/article/hype-chat-by-twitch">Official Help Article</a>
 */
@RequiredArgsConstructor
public enum HypeChatLevel {
    ONE(Duration.ofSeconds(30L)),
    TWO(Duration.ofSeconds(150L)),
    THREE(Duration.ofMinutes(5L)),
    FOUR(Duration.ofMinutes(10L)),
    FIVE(Duration.ofMinutes(30L)),
    SIX(Duration.ofHours(1L)),
    SEVEN(Duration.ofHours(2L)),
    EIGHT(Duration.ofHours(3L)),
    NINE(Duration.ofHours(4L)),
    TEN(Duration.ofHours(5L));

    @ApiStatus.Internal
    public static final Map<String, HypeChatLevel> MAPPINGS = EnumUtil.buildMapping(values());

    /**
     * The amount of time the message is pinned to the top of chat.
     */
    @Getter
    private final Duration pinDuration;

    /**
     * @return the level in integer form.
     */
    public int getLevel() {
        return this.ordinal() + 1;
    }

    /**
     * @return the maximum number of characters that can be in a hype chat message at this level.
     */
    public int getCharacterLimit() {
        return getLevel() * 50;
    }

    /**
     * @return whether the Hype Chat contribution has a colored gradient in the official chat interface.
     */
    public boolean hasGradient() {
        return this.ordinal() >= SIX.ordinal();
    }
}
