package me.philippheuer.twitch4j.message.pubsub;

import lombok.AllArgsConstructor;
import me.philippheuer.util.annotation.Unofficial;

@AllArgsConstructor
public enum PubSubType {
	/**
	 * Anyone cheers on a specified channel.
	 * <code>subject(&lt;channel ID&gt;)</code>
	 */
	CHANNEL_BITS("channel-bits-events-v1"),

	/**
	 * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
	 * <p>
	 * Subgift subscription messages contain recipient information.
	 * <code>subject(&lt;channel ID&gt;)</code>
	 */
	CHANNEL_SUBSCRIPTION("channel-subscribe-events-v1"),

	/**
	 * Anyone whispers the specified user.
	 * <code>subject(&lt;user ID&gt;)</code>
	 */
	WHISPERS("whispers"),

	/**
	 * Anyone follow on a specified channel.
	 * <code>subject(&lt;channel ID&gt;)</code>
	 */
	@Unofficial
	FOLLOW("following"),

	/**
	 * Listening moderation actions in specific channel.
	 * Owner ID must be a moderator in specific channel.
	 * <code>subject(&lt;owner ID&gt;, &lt;channel ID&gt;)</code>
	 */
	@Unofficial
	CHAT_MODERATION_ACTIONS("chat_moderator_actions"),

	/**
	 * Listens EBS broadcast sent to specific extension on a specific channel
	 * <code>subject(&lt;channel ID&gt;, &lt;extension ID&gt;)</code>
	 */
	@Unofficial
	CHANNEL_EXTENSION_BROADCAST("channel-ext-v1") {
		@Override
		public String subject(String... subject) {
			return super.subject(subject) + "-broadcast";
		}
	},

	/**
	 * Anyone makes a purchase on a channel.
	 * <code>subject(&lt;channel ID&gt;)</code>
	 */
	CHANNEL_COMMERCE("channel-commerce-events-v1"),

	/**
	 * Listening live stream with view counter in specific channel name
	 * <code>subject(&lt;channel name&gt;)</code>
	 */
	@Unofficial
	VIDEO_PLAYBACK("video-playback");

	private final String topic;

	public String subject(String... subject) {
		return String.format("%s.%s", topic, String.join(".", subject));
	}
}
