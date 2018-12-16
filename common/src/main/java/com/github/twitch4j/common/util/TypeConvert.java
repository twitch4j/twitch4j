package com.github.twitch4j.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TypeConvert {

    /**
     * ObjectMapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

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

}
