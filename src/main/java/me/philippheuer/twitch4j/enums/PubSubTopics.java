package me.philippheuer.twitch4j.enums;

import java.util.Arrays;
import java.util.stream.Collectors;
import me.philippheuer.twitch4j.model.Channel;

public enum PubSubTopics {
	BITS("channel-bits-events-v1", Scope.values()),
	SUBS("channel-subscribe-events-v1", Scope.CHANNEL_SUBSCRIPTIONS),
	COMMERCE("channel-commerce-events-v1", Scope.values()),
	WHISPERS("whispers", Scope.CHAT_LOGIN);

	private final String topic;
	private final Scope[] scope;

	PubSubTopics(String topic, Scope... scope) {
		this.topic = topic;
		this.scope = scope;
	}

	/**
	 * Getting name topics for the channel
	 * @param channel {@link Channel} model
	 * @return The topic that the message pertains to.
	 */
	public String getTopic(Channel channel) {
		return topic + "." + channel.getId();
	}

	public boolean isInRequiredScope(Scope... scope) {
		if (name().equalsIgnoreCase("subs") || name().equalsIgnoreCase("whispers")) {
			return Arrays.stream(scope).filter(this.scope::equals).collect(Collectors.toList()).isEmpty();
		} else return (scope.length > 0);
	}
}
