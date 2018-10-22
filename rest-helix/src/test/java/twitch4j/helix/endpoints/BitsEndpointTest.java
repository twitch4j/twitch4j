package twitch4j.helix.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import twitch4j.helix.domain.BitsLeaderboard;
import twitch4j.helix.domain.StreamList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class BitsEndpointTest extends AbtractEndpointTest {

    /**
     * Get Bits Leaderboard
     */
    @Test
    @DisplayName("Fetch the bits leaderboard")
    public void getBitsLeaderboard() {
        // TestCase
        BitsLeaderboard resultList = testUtils.getTwitchHelixClient().getBitsLeaderboard(testUtils.getCredential().getAuthToken(), "10", "all", null, null);

        System.out.println(resultList.toString());
        /*
        // Test
        assertTrue(resultList.getStreams().size() > 0, "Should at least find one result from the streams method!");
        resultList.getStreams().forEach(stream -> {
            assertNotNull(stream.getId(), "Id should not be null!");
            assertNotNull(stream.getUserId(), "UserId should not be null!");
            // assertNotNull(stream.getGameId(), "GameId should not be null!");
        });
        */
    }

}

