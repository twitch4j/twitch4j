package com.github.twitch4j.chat.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageParserTest {

    @Test
    void consumeLines() {
        assertEquals(singletonList("hello"), split("hello"));
        assertEquals(singletonList("hello "), split("hello "));
        assertEquals(singletonList("hello"), split("hello\n"));
        assertEquals(asList("hello", ""), split("hello\n\n"));
        assertEquals(singletonList("hello "), split("hello \n"));
        assertEquals(asList("hello ", " "), split("hello \n "));
        assertEquals(singletonList("hello"), split("hello\r\n"));
        assertEquals(asList("hello", ""), split("hello\r\n\r\n"));
        assertEquals(singletonList("hello "), split("hello \r\n"));
        assertEquals(asList("hello ", " "), split("hello \r\n "));

        assertEquals(singletonList("hello world"), split("hello world"));
        assertEquals(asList("hello", "world"), split("hello\nworld"));
        assertEquals(asList("hello", "world"), split("hello\r\nworld"));
        assertEquals(asList("hello", "world"), split("hello\r\nworld\n"));
        assertEquals(asList("hello", "world"), split("hello\r\nworld\r\n"));
        assertEquals(asList("hello", "world", "foo"), split("hello\r\nworld\r\nfoo"));
        assertEquals(asList("hello", "world", "foo"), split("hello\r\nworld\r\nfoo\n"));
        assertEquals(asList("hello", "world", "foo", "bar"), split("hello\r\nworld\r\nfoo\nbar"));
        assertEquals(asList("hello", "world", "foo", "bar"), split("hello\r\nworld\r\nfoo\nbar\r\n"));
        assertEquals(asList("hello", "world", "foo", "bar", "baz\r"), split("hello\r\nworld\r\nfoo\nbar\r\nbaz\r"));
    }

    private static List<String> split(String input) {
        List<String> lines = new ArrayList<>();
        MessageParser.consumeLines(input, lines::add);
        return lines;
    }

}
