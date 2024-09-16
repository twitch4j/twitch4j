package com.github.twitch4j.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.twitch4j.common.util.TwitchUtils.parseBadges;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unittest")
class TwitchUtilsTest {

    @Test
    @DisplayName("Tests TwitchUtils.parseBadges")
    void badgesParseTest() {
        assertEquals(emptyMap(), parseBadges(null));
        assertEquals(emptyMap(), parseBadges(""));

        assertEquals(singletonMap("subscriber", "15"), parseBadges("subscriber/15"));
        assertEquals(singletonMap("subscriber", "15/3"), parseBadges("subscriber/15/3"));
        assertEquals(singletonMap("a b", "c d"), parseBadges("a\\sb/c\\sd"));

        assertEquals(mapOf("subscriber", "18", "no_audio", "1"), parseBadges("subscriber/18,no_audio/1"));
        assertEquals(mapOf("subscriber", "19", "no_audio", null), parseBadges("subscriber/19,no_audio"));
        assertEquals(mapOf("subscriber", "19", "no_audio", ""), parseBadges("subscriber/19,no_audio/"));
        assertEquals(mapOf("follower", "20", "no_video", null), parseBadges("follower/20,no_video"));
    }

    private static <K, V> Map<K, V> mapOf(K key1, V value1, K key2, V value2) {
        Map<K, V> map = new HashMap<>(4);
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

}
