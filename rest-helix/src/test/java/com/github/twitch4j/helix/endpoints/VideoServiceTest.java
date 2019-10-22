package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.domain.VideoList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class VideoServiceTest extends AbtractEndpointTest {

    // Overwatch GameId
    private static String overwatchGameId = "488552l";

    /**
     * Get Videos
     */
    @Test
    @DisplayName("Fetch videos")
    public void getVideos() {
        // TestCase
        VideoList resultList = testUtils.getTwitchHelixClient().getVideos(null, null, null, overwatchGameId, null, null, null, null, null, null, 100).execute();

        // Test
        assertTrue(resultList.getVideos().size() > 0, "Should at least find one result from the videos method!");
        resultList.getVideos().forEach(video -> {
            assertNotNull(video.getId());
            assertNotNull(video.getUserId());
            assertNotNull(video.getUserName());
        });
    }

}
