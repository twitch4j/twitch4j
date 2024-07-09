package com.github.twitch4j.common.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unittest")
class EscapeUtilsTest {

    @Test
    void unescapeTagValue() {
        assertEquals("", EscapeUtils.unescapeTagValue(""));
        assertEquals(" ", EscapeUtils.unescapeTagValue("\\s"));
        assertEquals("  ", EscapeUtils.unescapeTagValue("\\s\\s"));
        assertEquals(" . ", EscapeUtils.unescapeTagValue("\\s.\\s"));
        assertEquals(". .", EscapeUtils.unescapeTagValue(".\\s."));
        assertEquals(" ;\r\n\\", EscapeUtils.unescapeTagValue("\\s\\:\\r\\n\\\\"));
        assertEquals(" ;\r\n\\ ", EscapeUtils.unescapeTagValue("\\s\\:\\r\\n\\\\\\s"));
        assertEquals("\\s", EscapeUtils.unescapeTagValue("\\\\s"));
        assertEquals("a", EscapeUtils.unescapeTagValue("\\a"));
        assertEquals("aaa:", EscapeUtils.unescapeTagValue("\\aaa:"));
        assertEquals("aaa;", EscapeUtils.unescapeTagValue("\\aaa\\:"));
        assertEquals("HelloWorld!", EscapeUtils.unescapeTagValue("HelloWorld!"));
        assertEquals("HelloWorld!", EscapeUtils.unescapeTagValue("HelloWorld!\\"));
        assertEquals("HelloWorld!", EscapeUtils.unescapeTagValue("Hell\\oWorld!"));
        assertEquals("Hello World!", EscapeUtils.unescapeTagValue("Hello\\sWorld!"));
        assertEquals("Hello W world!", EscapeUtils.unescapeTagValue("Hello\\sW\\sworld!"));
        assertEquals("Hello  World!", EscapeUtils.unescapeTagValue("Hello\\s\\sWorld!"));
        assertEquals("Hello  World;", EscapeUtils.unescapeTagValue("Hello\\s\\sWorld\\:"));
        assertEquals("Hello  World!", EscapeUtils.unescapeTagValue("Hello\\s\\sWorld!\\"));
    }

}
