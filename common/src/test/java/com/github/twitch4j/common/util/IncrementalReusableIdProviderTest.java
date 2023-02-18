package com.github.twitch4j.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncrementalReusableIdProviderTest {

    @Test
    void testGet() {
        IncrementalReusableIdProvider provider = new IncrementalReusableIdProvider();
        assertEquals("0", provider.get());
        assertEquals("1", provider.get());
        assertEquals("2", provider.get());
    }

    @Test
    void testRelease() {
        IncrementalReusableIdProvider provider = new IncrementalReusableIdProvider();
        assertEquals("0", provider.get());
        assertEquals("1", provider.get());
        assertEquals("2", provider.get());

        provider.release("0");
        provider.release("1");

        assertEquals("0", provider.get());
        assertEquals("1", provider.get());
        assertEquals("3", provider.get());
    }

}
