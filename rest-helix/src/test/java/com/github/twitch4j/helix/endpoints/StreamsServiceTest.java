package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.IngestServerList;
import com.github.twitch4j.helix.domain.StreamKey;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.StreamMarkersList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        StreamList resultList = testUtils.getTwitchHelixClient().getStreams(null, "", "", 5, null, null, null, null).execute();

        // Test
        assertTrue(resultList.getStreams().size() > 0, "Should at least find one result from the streams method!");
        resultList.getStreams().forEach(stream -> {
            assertNotNull(stream.getId(), "Id should not be null!");
            assertNotNull(stream.getUserId(), "UserId should not be null!");
            assertNotNull(stream.getUserName(), "UserName should not be null!");
            // assertNotNull(stream.getGameId(), "GameId should not be null!");
        });
    }

    @Test
    @DisplayName("Fetch the user's stream key")
    public void getStreamKey() {
        List<StreamKey> resultList = TestUtils.getTwitchHelixClient().getStreamKey(TestUtils.getCredential().getAccessToken(), twitchUserId).execute().getKeys();
        Assertions.assertFalse(resultList.isEmpty());
        resultList.forEach(key -> Assertions.assertNotNull(key.getStreamKey()));
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

    @Test
    @DisplayName("getIngestServers")
    public void getIngestServers() {
        IngestServerList serverList = TestUtils.getTwitchHelixClient().getIngestServers().execute();
        assertNotNull(serverList.getIngests());
        assertFalse(serverList.getIngests().isEmpty());
        assertNotNull(serverList.getIngests().get(0).getName());
        assertNotNull(serverList.getIngests().get(0).getUrlTemplate());
    }

}
