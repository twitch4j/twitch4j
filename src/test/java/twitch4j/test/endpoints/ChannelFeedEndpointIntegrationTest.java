package twitch4j.test.endpoints;

import java.util.List;
import twitch4j.model.ChannelFeedPost;
import twitch4j.test.TwitchClientIntegrationTest;
import twitch4j.test.model.AssertEntity;
import twitch4j.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.util.Assert;

@Category(IntegrationTestCategory.class)
public class ChannelFeedEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Test the Get UserId by Name Method
	 */
	@Test
	public void testGetFeedPosts() {
		List<ChannelFeedPost> channelFeedPosts = twitchClient.getChannelFeedEndpoint().getFeedPosts(CHANNEL_ID, null, null, null);

		// Result
		Assert.isTrue(channelFeedPosts != null && channelFeedPosts.size() > 0, "No posts found!");
		for (ChannelFeedPost post : channelFeedPosts) {
			//System.out.println(post.toString());
			AssertEntity.assertChannelFeedPost(post);
		}
	}

	/**
	 * Test the Get UserId by Name Method
	 */
	@Test
	public void testGetFeedPost() {
		String postId = "c5527e8a-7cb5-40ca-b434-160ef0bd0b1b";

		ChannelFeedPost post = twitchClient.getChannelFeedEndpoint().getFeedPost(CHANNEL_ID, postId, null);
		// System.out.println(post);

		AssertEntity.assertChannelFeedPost(post);
	}

	/**
	 * Test the Create Post in Feed method
	 */
	@Test
	public void getCreateFeedPost() {
		if(USER_OAUTH_TOKEN.length() <= 0) {
			return;
		}
	}

}
