package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.test.TwitchClientTestCase;
import org.junit.Test;

import java.util.Optional;

public class UserEndpointTest extends TwitchClientTestCase {

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
