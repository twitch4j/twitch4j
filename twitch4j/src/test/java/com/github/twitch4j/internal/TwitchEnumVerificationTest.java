package com.github.twitch4j.internal;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.github.twitch4j.common.enums.TwitchEnum;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
class TwitchEnumVerificationTest {

    @Test
    void validateTwitchEnumHasDefaultValue() {
        findTwitchEnumGenericTypes().forEach(clazz -> {
            assertNotNull(clazz, "Raw use of TwitchEnum detected");

            boolean hasDefaultValue = Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(field -> field.isAnnotationPresent(JsonEnumDefaultValue.class));
            assertTrue(hasDefaultValue, "TwitchEnum is missing @JsonEnumDefaultValue: " + clazz);
        });
    }

    private Stream<? extends Class<?>> findTwitchEnumGenericTypes() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .forPackages("com.github.twitch4j")
            .addScanners(Scanners.SubTypes.filterResultsBy(s -> true))
        );

        return reflections.getSubTypesOf(Object.class).stream()
            .flatMap(clazz -> Arrays.stream(clazz.getDeclaredFields()))
            .filter(field -> field.getType() == TwitchEnum.class)
            .map(field -> {
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                        return (Class<?>) typeArguments[0];
                    }
                }
                return null;
            });
    }

}
