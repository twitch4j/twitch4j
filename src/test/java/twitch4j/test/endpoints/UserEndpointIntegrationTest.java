package twitch4j.test.endpoints;

import twitch4j.model.User;
import twitch4j.test.TwitchClientIntegrationTest;
import twitch4j.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTestCategory.class)
public class UserEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Test the Get UserId by Name Method
	 */
	@Test
	public void testGetUserIdByName() {
		Long userId = twitchClient.getUserEndpoint().getUserIdByUserName("twitch4j");

		assertNotNull(userId);
		assertEquals(149223493L, (long) userId);
	}

	/**
	 * Test the Get User Method
	 */
	@Test
	public void testGetUser() {
		User user = twitchClient.getUserEndpoint().getUser(149223493L);

		assertNotNull(user);
		assertNotNull(user.getId());
		assertNotNull(user.getName());
		assertNotNull(user.getDisplayName());
	}

}
