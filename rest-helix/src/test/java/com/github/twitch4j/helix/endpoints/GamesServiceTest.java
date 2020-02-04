package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.domain.GameList;
import com.github.twitch4j.helix.domain.GameTopList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class GamesServiceTest extends AbstractEndpointTest {

    /** Overwatch GameId */
    private static String overwatchGameId = "488552";

    @Test
    @DisplayName("Get Games")
    public void getGames() {
        // TestCase
        GameList resultList = testUtils.getTwitchHelixClient().getGames(null, Arrays.asList(overwatchGameId), null).execute();

        // Test
        assertTrue(resultList.getGames().size() > 0, "Should at least find one result from the streams method!");
        resultList.getGames().forEach(game -> {
            assertNotNull(game.getId(), "Id should not be null!");
            assertNotNull(game.getName(), "UserId should not be null!");
        });
    }

    @Test
    @DisplayName("Get Top Games")
    public void getTopGames() {
        // TestCase
        GameTopList resultList = testUtils.getTwitchHelixClient().getTopGames(null, null, null, null).execute();

        // Test
        assertTrue(resultList.getGames().size() > 0, "Should at least find one result from the getTopGames method!");
        resultList.getGames().forEach(game -> {
            assertNotNull(game.getId(), "Id should not be null!");
            assertNotNull(game.getName(), "UserId should not be null!");
        });
    }
}

