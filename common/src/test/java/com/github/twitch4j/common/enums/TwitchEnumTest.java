package com.github.twitch4j.common.enums;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.TwitchEnumDeserializer;
import com.github.twitch4j.common.util.TypeConvert;
import lombok.Data;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unittest")
class TwitchEnumTest {

    @Test
    void deserialize() {
        String json = "{\"a\":\"X\"}";
        TwitchEnum<A> twitchEnum = TypeConvert.jsonToObject(json, B.class).getTwitchEnum();
        assertEquals(A.X, twitchEnum.getValue());
        assertEquals("X", twitchEnum.getRawValue());
    }

    @Test
    void deserializeCasing() {
        String json = "{\"a\":\"y\"}";
        TwitchEnum<A> twitchEnum = TypeConvert.jsonToObject(json, B.class).getTwitchEnum();
        assertEquals(A.Y, twitchEnum.getValue());
        assertEquals("y", twitchEnum.getRawValue());
    }

    @Test
    void deserializeAbsent() {
        String json = "{}";
        TwitchEnum<A> twitchEnum = TypeConvert.jsonToObject(json, B.class).getTwitchEnum();
        assertNull(twitchEnum);
    }

    @Test
    void deserializeNull() {
        String json = "{\"a\":null}";
        TwitchEnum<A> twitchEnum = TypeConvert.jsonToObject(json, B.class).getTwitchEnum();
        assertNull(twitchEnum);
    }

    @Test
    void deserializeUnknown() {
        String json = "{\"a\":\"W\"}";
        TwitchEnum<A> twitchEnum = TypeConvert.jsonToObject(json, B.class).getTwitchEnum();
        assertEquals(A.UNKNOWN, twitchEnum.getValue());
        assertEquals("W", twitchEnum.getRawValue());
    }

    @Test
    void deserializeNamed() {
        String json = "{\"a\":\"test\"}";
        TwitchEnum<A> twitchEnum = TypeConvert.jsonToObject(json, B.class).getTwitchEnum();
        assertEquals(A.Z, twitchEnum.getValue());
        assertEquals("test", twitchEnum.getRawValue());
    }

    @Test
    void deserializeAlias() {
        String json = "{\"a\":\"Q\"}";
        TwitchEnum<A> twitchEnum = TypeConvert.jsonToObject(json, B.class).getTwitchEnum();
        assertEquals(A.Z, twitchEnum.getValue());
        assertEquals("Q", twitchEnum.getRawValue());
    }

    enum A {
        X,
        Y,
        @JsonAlias("Q")
        @JsonProperty("test")
        Z,
        @JsonEnumDefaultValue
        UNKNOWN
    }

    @Data
    static class B {
        @JsonProperty("a")
        @JsonDeserialize(using = TwitchEnumDeserializer.class)
        TwitchEnum<A> twitchEnum;
    }

}
