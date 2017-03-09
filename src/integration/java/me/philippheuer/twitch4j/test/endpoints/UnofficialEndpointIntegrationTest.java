package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.model.unofficial.Ember;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTestCategory.class)
public class UnofficialEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Integration Test: Get Ember Result
	 */
	@Test
	public void testGetEmber() {
		Ember ember = twitchClient.getUnofficialEndpoint().getEmber("a_seagull");

		// Result
		assertNotNull(ember.getPrimaryTeamName());
		assertNotNull(ember.getPrimaryTeamDisplayName());
	}

	/**
	 * Integration Test: Get linked Steam Profile Id
	 */
	@Test
	public void testGetSteamProfile() {
		String profileId = twitchClient.getUnofficialEndpoint().getConnectedSteamProfile("a_seagull");

		// Result
		assertNotNull(profileId);
	}

}
