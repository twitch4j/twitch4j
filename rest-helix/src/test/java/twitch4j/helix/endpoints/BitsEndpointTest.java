package twitch4j.helix.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import twitch4j.helix.domain.BitsLeaderboard;

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
        BitsLeaderboard resultList = testUtils.getTwitchHelixClient().getBitsLeaderboard(testUtils.getCredential().getAuthToken(), "10", "all", null, null).execute();

        // Test
        assertTrue(resultList.getEntries().size() == 0, "That account can't get bits, so it's always a empty list");
    }

}

