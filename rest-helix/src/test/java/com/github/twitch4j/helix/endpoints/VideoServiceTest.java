package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.DeletedVideoList;
import com.github.twitch4j.helix.domain.Video;
import com.github.twitch4j.helix.domain.VideoList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class VideoServiceTest extends AbstractEndpointTest {

    /** Overwatch GameId */
    private static final String overwatchGameId = "515025";

    @Test
    @DisplayName("Fetch videos by ID")
    public void getVideos() {
        List<String> ids = Arrays.asList("806178786", "806178788");
        List<Video> videos = TestUtils.getTwitchHelixClient().getVideos(TestUtils.getCredential().getAccessToken(), ids, null, null, null, null, null, null, null, null, null)
            .execute()
            .getVideos();
        assertNotNull(videos);
        assertEquals(ids.size(), videos.size());
        videos.forEach(video -> {
            assertNotNull(video.getId());
            assertNotNull(video.getUserId());
            assertNotNull(video.getUserName());
        });
    }

    /**
     * Get Videos
     */
    @Test
    @DisplayName("Fetch videos by game ID")
    public void getVideosByGame() {
        // TestCase
        VideoList resultList = TestUtils.getTwitchHelixClient().getVideos(TestUtils.getCredential().getAccessToken(), null, null, overwatchGameId, null, null, null, null, 100, null, null).execute();

        // Test
        assertTrue(resultList.getVideos().size() > 0, "Should at least find one result from the videos method!");
        resultList.getVideos().forEach(video -> {
            assertNotNull(video.getId());
            assertNotNull(video.getUserId());
            assertNotNull(video.getUserName());
        });
    }

    @Test
    @DisplayName("Delete videos")
    @Disabled
    public void deleteVideos() {
        DeletedVideoList resultList = TestUtils.getTwitchHelixClient().deleteVideos(TestUtils.getCredential().getAccessToken(), Collections.singletonList("806649844")).execute();
        assertNotNull(resultList);
        assertNotNull(resultList.getDeletedVideoIds());
    }

}
