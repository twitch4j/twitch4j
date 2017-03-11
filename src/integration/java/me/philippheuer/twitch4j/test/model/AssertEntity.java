package me.philippheuer.twitch4j.test.model;

import me.philippheuer.twitch4j.model.ChannelFeedPost;
import org.springframework.util.Assert;

public class AssertEntity {

	/**
	 * Checks the provided model type.
	 *
	 * @param entity ChannelFeedPost
	 */
	public static void assertChannelFeedPost(ChannelFeedPost entity) {
		Assert.notNull(entity);
		Assert.notNull(entity.getBody());
		Assert.notNull(entity.getId());
		Assert.notNull(entity.getUser());
		Assert.notNull(entity.getCreatedAt());
		Assert.notNull(entity.getDeleted());
	}

}
