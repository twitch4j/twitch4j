package io.twitch4j.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class SimpleTypeReference<T> extends TypeReference<T> {
    @Getter
    private final Type type;
}
