package com.github.twitch4j.chat.flag;

import lombok.RequiredArgsConstructor;

/**
 * The AutoMod moderation categories.
 *
 * @see <a href="https://help.twitch.tv/s/article/how-to-use-automod">Official documentation</a>
 */
@RequiredArgsConstructor
public enum FlagType {
    /**
     * Identity language - Words referring to race, religion, gender, orientation, disability, or similar. Hate speech falls under this category.
     */
    IDENTITY('I'),
    /**
     * Sexually explicit language - Words or phrases referring to sexual acts, sexual content, and body parts.
     */
    SEXUAL('S'),
    /**
     * Aggressive language - Hostility towards other people, often associated with bullying.
     */
    AGGRESSIVE('A'),
    /**
     * Profanity - Expletives, curse words, and vulgarity. This filter especially helps those who wish to keep their community family-friendly.
     */
    PROFANITY('P');

    final char code;

    private static final FlagType[] VALUES = values();

    public static FlagType parse(final String string) {
        if (string == null || string.length() != 1)
            return null;

        final char match = string.charAt(0);
        for (FlagType type : VALUES) {
            if (type.code == match)
                return type;
        }

        return null;
    }
}
