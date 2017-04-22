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
		Assert.notNull(entity, "Entity can't be null.");
		Assert.notNull(entity.getBody(), "Entity does not have a body.");
		Assert.notNull(entity.getId(), "Entity does not have an id.");
		Assert.notNull(entity.getUser(), "Entity does not have information about the user.");
		Assert.notNull(entity.getCreatedAt(), "Entity does not have the created property.");
		Assert.notNull(entity.getDeleted(), "Entity does not have the deleted property.");
	}

}
