package twitch4j.test.endpoints;

import java.util.List;
import twitch4j.model.Follow;
import twitch4j.test.TwitchClientIntegrationTest;
import twitch4j.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.util.Assert;

@Category(IntegrationTestCategory.class)
public class ChannelEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Test the GetFollowers Method
	 */
	@Test
	public void testGetFollowers() {
		List<Follow> followList = twitchClient.getChannelEndpoint().getFollowers(CHANNEL_ID, 100, null);

		// Result
		assertNotNull(followList);
		assertTrue(followList.size() > 0);

		// Check Entities
		for(Follow f : followList) {
			Assert.notNull(f.getUser(), "User property can't be null.");
			Assert.notNull(f.getCreatedAt(), "CreatedAt property can't be null.");
			Assert.notNull(f.getChannel(), "Channel property can't be null.");
			Assert.notNull(f.getNotifications(), "Notification property can't be null.");
		}
	}

}
