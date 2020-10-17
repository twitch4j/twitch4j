package com.github.twitch4j.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Getter;

public class TypeConvert {

    /**
     * ObjectMapper
     */
    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new JavaTimeModule());

    public static String objectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T jsonToObject(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        try {
            return objectMapper.convertValue(fromValue, toValueTypeRef);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
