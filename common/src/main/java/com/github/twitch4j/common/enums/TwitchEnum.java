package com.github.twitch4j.common.enums;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapper for a Twitch-specified enum where all of the possible values may not be documented.
 *
 * @param <E> the underlying enum type
 */
@Value
public class TwitchEnum<E extends Enum<E>> {

    /**
     * The parsed enum value.
     */
    @NotNull
    E value;

    /**
     * The raw string provided by Twitch to represent this enum.
     * <p>
     * This field is useful when {@link #getValue()} yields {@code UNKNOWN}.
     * Please report such cases to our issue tracker.
     */
    @NotNull
    String rawValue;

}
