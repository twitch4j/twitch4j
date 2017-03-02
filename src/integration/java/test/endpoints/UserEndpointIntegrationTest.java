package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Optional;

@Category(IntegrationTestCategory.class)
public class UserEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Test the Get UserId by Name Method
	 */
	@Test
	public void testGetUserIdByName() {
		Optional<Long> userId = twitchClient.getUserEndpoint().getUserIdByUserName("twitch4j");

		assertTrue(userId.isPresent());
		assertEquals("149223493", userId.get().toString());
	}

}
