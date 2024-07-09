package com.github.twitch4j.helix.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
class StreamTest {

    @Test
    void deserializeLive() {
        Stream stream = TypeConvert.jsonToObject(
            "{\"game_id\":\"509658\",\"game_name\":\"Just Chatting\",\"id\":\"44096493851\",\"is_mature\":true,\"language\":\"en\",\"started_at\":\"2024-04-26T18:28:16Z\",\"tag_ids\":[],\"tags\":[\"news\",\"politics\",\"adhd\",\"English\"],\"thumbnail_url\":\"https://static-cdn.jtvnw.net/previews-ttv/live_user_hasanabi-{width}x{height}.jpg\",\"title\":\"RECAPPING UCLA FREE PALESTINE PROTEST - VIOLENT EMORY PROFESSOR ARREST - BRANDON ON HOWARD STERN - WALKING DEAD S3 FINALE!\",\"type\":\"live\",\"user_id\":\"207813352\",\"user_login\":\"hasanabi\",\"user_name\":\"HasanAbi\",\"viewer_count\":22648}",
            Stream.class
        );
        assertEquals("509658", stream.getGameId());
        assertEquals("Just Chatting", stream.getGameName());
        assertEquals("44096493851", stream.getId());
        assertTrue(stream.isMature());
        assertEquals("en", stream.getLanguage());
        assertEquals(Instant.parse("2024-04-26T18:28:16Z"), stream.getStartedAtInstant());
        assertEquals(Arrays.asList("news", "politics", "adhd", "English"), stream.getTags());
        assertEquals("https://static-cdn.jtvnw.net/previews-ttv/live_user_hasanabi-{width}x{height}.jpg", stream.getThumbnailUrlTemplate());
        assertEquals("https://static-cdn.jtvnw.net/previews-ttv/live_user_hasanabi-1280x720.jpg", stream.getThumbnailUrl(1280, 720));
        assertEquals("RECAPPING UCLA FREE PALESTINE PROTEST - VIOLENT EMORY PROFESSOR ARREST - BRANDON ON HOWARD STERN - WALKING DEAD S3 FINALE!", stream.getTitle());
        assertEquals("207813352", stream.getUserId());
        assertEquals("HasanAbi", stream.getUserName());
        assertEquals(22648, stream.getViewerCount());
    }

}
