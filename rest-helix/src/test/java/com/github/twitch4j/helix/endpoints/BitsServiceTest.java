package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.BitsLeaderboard;
import com.github.twitch4j.helix.domain.Cheermote;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class BitsServiceTest extends AbstractEndpointTest {

    /**
     * Get Bits Leaderboard
     */
    @Test
    @DisplayName("Fetch the bits leaderboard")
    public void getBitsLeaderboard() {
        // TestCase
        BitsLeaderboard resultList = testUtils.getTwitchHelixClient().getBitsLeaderboard(testUtils.getCredential().getAccessToken(), "10", "all", null, null).execute();

        // Test
        assertTrue(resultList.getEntries().size() == 0, "That account can't get bits, so it's always a empty list");
    }

    @Test
    @DisplayName("Retrieves the list of available Cheermotes")
    public void getCheermotes() {
        List<Cheermote> resultList = TestUtils.getTwitchHelixClient().getCheermotes(TestUtils.getCredential().getAccessToken(), "12826").execute().getCheermotes();
        Assertions.assertFalse(resultList.isEmpty());
    }

}
