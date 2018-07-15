package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.model.tmi.Chatter;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * These tests check the unofficial tmi endpoints.
 */
@Category(IntegrationTestCategory.class)
public class TMIEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Integration Test: Get Chatters
	 */
	@Test
	public void testGetChatters() {
		// Request
		Chatter chatter = twitchClient.getTMIEndpoint().getChatters(USER_NAME);

		// Result
		assertNotNull(chatter);
		assertNotNull(chatter.getAdmins());
		assertNotNull(chatter.getGlobalMods());
		assertNotNull(chatter.getModerators());
		assertNotNull(chatter.getStaff());
		assertNotNull(chatter.getViewers());
	}

}
