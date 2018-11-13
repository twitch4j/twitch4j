package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.StreamMarkersList;
import com.github.twitch4j.helix.domain.StreamMetadata;
import com.github.twitch4j.helix.domain.StreamMetadataList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class StreamsServiceTest extends AbtractEndpointTest {

    // Hearthstone GameId
    private static String hearthstoneGameId = "138585";

    // Overwatch GameId
    private static String overwatchGameId = "488552";

    /**
     * Get Streams
     */
    @Test
    @DisplayName("Fetch information about current live streams")
    public void getStreams() {
        // TestCase
        StreamList resultList = testUtils.getTwitchHelixClient().getStreams("", "", 5, null, null, null, null, null).execute();

        // Test
        assertTrue(resultList.getStreams().size() > 0, "Should at least find one result from the streams method!");
        resultList.getStreams().forEach(stream -> {
            assertNotNull(stream.getId(), "Id should not be null!");
            assertNotNull(stream.getUserId(), "UserId should not be null!");
            // assertNotNull(stream.getGameId(), "GameId should not be null!");
        });
    }

    /**
     * Get Stream Metadata (Hearthstone)
     */
    @Test
    @DisplayName("Fetch meta-information (hearthstone) about live streams")
    public void getStreamMetadataForHearthstone() {
        // TestCase
        StreamMetadataList resultList = testUtils.getTwitchHelixClient().getStreamsMetadata("", "", 5, null, Arrays.asList(hearthstoneGameId), null, null, null).execute();

        // Test
        assertTrue(resultList.getStreams().size() > 0, "Should at least find one result from the streams metadata method!");
        int foundMetadata = 0;
        for (StreamMetadata stream : resultList.getStreams()) {
            if (stream.getHearthstone() != null) {
                foundMetadata++;
            }
        }
        assertTrue(foundMetadata > 0, "Hearthstone Metadata should be provided!");
    }

    /**
     * Get Stream Metadata (Overwatch)
     */
    @Test
    @DisplayName("Fetch meta-information (overwatch) about live streams")
    public void getStreamMetadataForOverwatch() {
        // TestCase
        StreamMetadataList resultList = testUtils.getTwitchHelixClient().getStreamsMetadata("", "", 5, null, Arrays.asList(overwatchGameId), null, null, null).execute();

        // Test
        assertTrue(resultList.getStreams().size() > 0, "Should at least find one result from the streams metadata method!");
        int foundMetadata = 0;
        for (StreamMetadata stream : resultList.getStreams()) {
            if (stream.getOverwatch() != null) {
                foundMetadata++;
            }
        }
        assertTrue(foundMetadata > 0, "Overwatch Metadata should be provided!");
    }

    /**
     * Get Stream Markers
     */
    @Test
    @DisplayName("Fetch stream markers")
    @Disabled
    public void getStreamMarkers() {
        // TestCase
        StreamMarkersList resultList = testUtils.getTwitchHelixClient().getStreamMarkers("", "", "", null, "217359661l", "137512364l").execute();

        // Test
        assertTrue(resultList.getStreamMarkers().size() > 0, "Should at least find one result from the streams metadata method!");
        resultList.getStreamMarkers().forEach(stream -> {
            stream.getVideos().forEach(videoMarker -> {
                videoMarker.getMarkers().forEach(marker -> {
                    System.out.println(marker.getId() + ":" + marker.getDescription());
                });
            });
        });
    }
}

