package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.model.TopGame;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

@Category(IntegrationTestCategory.class)
public class GameEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Test the Get UserId by Name Method
	 */
	@Test
	public void testGetTopGames() {
		List<TopGame> gameList = twitchClient.getGameEndpoint().getTopGames();

		// Result
		assertNotNull(gameList);
		assertTrue(gameList.size() > 0);
	}

}
