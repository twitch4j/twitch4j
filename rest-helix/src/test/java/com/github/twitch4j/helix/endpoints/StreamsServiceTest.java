package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class StreamsServiceTest extends AbstractEndpointTest {

    /** UserId */
    private static String twitchUserId = "149223493";

    /** Hearthstone GameId */
    private static String hearthstoneGameId = "138585";

     /** Overwatch GameId */
    private static String overwatchGameId = "488552";

    /**
     * Get Streams
     */
    @Test
    @DisplayName("Fetch information about current live streams")
    public void getStreams() {
        // TestCase
        StreamList resultList = testUtils.getTwitchHelixClient().getStreams(null, "", "", 5, null, null, null, null, null).execute();

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
        StreamMetadataList resultList = testUtils.getTwitchHelixClient().getStreamsMetadata(null, "", "", 5, null, Arrays.asList(hearthstoneGameId), null, null, null).execute();

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
        StreamMetadataList resultList = testUtils.getTwitchHelixClient().getStreamsMetadata(null, "", "", 5, null, Arrays.asList(overwatchGameId), null, null, null).execute();

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
        StreamMarkersList resultList = testUtils.getTwitchHelixClient().getStreamMarkers("", "", "", null, "217359661", "137512364").execute();

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

    /**
     * getAllStreamTags
     */
    @Test
    @DisplayName("getAllStreamTags")
    public void getAllStreamTags() {
        // TestCase
        StreamTagList resultList = testUtils.getTwitchHelixClient().getAllStreamTags(null, "", 100, null).execute();

        // Test
        assertTrue(resultList.getStreamTags().size() > 0, "Should at least find one stream tag!");
        resultList.getStreamTags().forEach(tag -> {
            assertTrue(tag.getTagId() != null, "TagId should not be null");
        });
    }

    /**
     * Gets stream tags which are active on the specified stream.
     */
    @Test
    @DisplayName("getStreamTagsOfStream")
    @Disabled
    public void getStreamTagsOfStream() {
        // TestCase
        StreamTagList resultList = testUtils.getTwitchHelixClient().getStreamTags(null, twitchUserId).execute();

        // Test
        assertTrue(resultList.getStreamTags().size() == 1, "Should have exactly 1 stream tag!");
        resultList.getStreamTags().forEach(tag -> {
            assertTrue(tag.getTagId().equals(UUID.fromString("a59f1e4e-257b-4bd0-90c7-189c3efbf917")), "TagId doesn't match a59f1e4e-257b-4bd0-90c7-189c3efbf917");
            assertTrue(tag.getLocalizationNames().get("en-us").equalsIgnoreCase("programming"), "tag en-us name should equal programming!");
            assertTrue(tag.getLocalizationDescriptions().get("en-us").equalsIgnoreCase("For streams with an emphasis on the discussion or process of computer programming"), "tag en-us description doesn't match!");
        });
    }

    /**
     * replaceStreamTags
     */
    @Test
    @DisplayName("replaceStreamTags")
    @Disabled
    public void replaceStreamTags() {
        // TestCase
        List<UUID> tagIds = new ArrayList<>();
        tagIds.add(UUID.fromString("a59f1e4e-257b-4bd0-90c7-189c3efbf917"));

        testUtils.getTwitchHelixClient().replaceStreamTags(testUtils.getCredential().getAccessToken(), twitchUserId, tagIds).execute();
    }

}

