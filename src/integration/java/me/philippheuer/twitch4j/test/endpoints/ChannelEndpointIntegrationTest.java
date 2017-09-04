package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.model.Follow;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Category(IntegrationTestCategory.class)
public class ChannelEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Test the GetFollowers Method
	 */
	@Test
	public void testGetFollowers() {
		List<Follow> followList = twitchClient.getChannelEndpoint(CHANNEL_ID).getFollowers(Optional.ofNullable(100l), Optional.empty());

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
