package com.github.twitch4j.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class EnumUtil {

    /**
     * Transforms enum values to a key-value map based on the specified functions.
     *
     * @param values      the values within an enum to be transformed
     * @param keySelector a function that derives a key associated to an enum entry
     * @param valueMapper a function that yields a value for a given enum entry
     * @param <E>         the type of enum
     * @param <K>         the type of keys
     * @param <V>         the type of values
     * @return a {@link Map} of key-value associations from transforming the specified enum entries
     * @apiNote The key derivation function should not yield collisions (even though we do not throw an exception upon a duplicate key).
     * @implSpec The returned map is unmodifiable. The underlying map is {@link HashMap} so that null keys (and values) are supported.
     * @implNote Enum#values is used instead of {@link Class#getEnumConstants()} to avoid reflection overhead.
     */
    public <E extends Enum<E>, K, V> Map<K, V> buildMapping(@NotNull E[] values, @NotNull Function<E, K> keySelector, @NotNull Function<E, V> valueMapper) {
        final Map<K, V> map = new HashMap<>((values.length * 4 + 2) / 3);
        for (E e : values) {
            map.putIfAbsent(keySelector.apply(e), valueMapper.apply(e));
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Creates a mapping of enum values via an arbitrary key derivation function.
     *
     * @param values      the values within an enum to be mapped
     * @param keySelector a function that derives a key associated to an enum value
     * @param <E>         the type of enum
     * @param <K>         the type of keys
     * @return the mapping of enum values by the derived keys
     * @see #buildMapping(Enum[], Function, Function)
     */
    public <E extends Enum<E>, K> Map<K, E> buildMapping(@NotNull E[] values, @NotNull Function<E, K> keySelector) {
        return buildMapping(values, keySelector, Function.identity());
    }

    /**
     * Creates a mapping of enum values by their string representation.
     *
     * @param values the values within an enum to be mapped
     * @param <E>    the type of enum
     * @return a mapping of {@link Enum#toString()} to the enum value itself
     * @see #buildMapping(Enum[], Function)
     */
    public <E extends Enum<E>> Map<String, E> buildMapping(@NotNull E[] values) {
        return buildMapping(values, Enum::toString);
    }

}
