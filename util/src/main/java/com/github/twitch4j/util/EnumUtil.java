package com.github.twitch4j.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class EnumUtil {

    public <E extends Enum<E>, K, V> Map<K, V> buildMapping(E[] values, Function<E, K> keySelector, Function<E, V> valueMapper) {
        return Collections.unmodifiableMap(
            Arrays.stream(values).collect(
                Collectors.toMap(keySelector, valueMapper, (a, b) -> a, HashMap::new)
            )
        );
    }

    public <E extends Enum<E>, K> Map<K, E> buildMapping(E[] values, Function<E, K> keySelector) {
        return buildMapping(values, keySelector, Function.identity());
    }

    public <E extends Enum<E>> Map<String, E> buildMapping(E[] values) {
        return buildMapping(values, Enum::toString);
    }

}
