package me.philippheuer.twitch4j.test.endpoints;

import java.util.List;
import me.philippheuer.twitch4j.model.TopGame;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTestCategory.class)
public class GameEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Test the Get UserId by Name Method
	 */
	@Test
	public void testGetTopGames() {
		List<TopGame> gameList = twitchClient.getGameEndpoint().getTopGames(null, null);

		// Result
		assertNotNull(gameList);
		assertTrue(gameList.size() > 0);
	}

}
