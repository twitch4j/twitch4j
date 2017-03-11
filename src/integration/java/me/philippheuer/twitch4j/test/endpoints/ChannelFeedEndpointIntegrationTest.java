package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.model.ChannelFeedPost;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Category(IntegrationTestCategory.class)
public class ChannelFeedEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Test the Get UserId by Name Method
	 */
	@Test
	public void testGetFeedPosts() {
		List<ChannelFeedPost> channelFeedPosts = twitchClient.getChannelFeedEndpoint().getFeedPosts(CHANNEL_ID, Optional.empty(), Optional.empty(), Optional.empty());

		for (ChannelFeedPost post : channelFeedPosts) {
			System.out.println(post.toString());
		}

		// Result
		Assert.notNull(channelFeedPosts);
		Assert.isTrue(channelFeedPosts.size() > 0);
		for (ChannelFeedPost post : channelFeedPosts) {
			Assert.notNull(post.getBody());
			Assert.notNull(post.getId());
			Assert.notNull(post.getUser());
			Assert.notNull(post.getCreatedAt());
			Assert.notNull(post.getDeleted());
		}
	}

}
