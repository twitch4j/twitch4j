package me.philippheuer.twitch4j.enums;

import me.philippheuer.twitch4j.model.Channel;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum PubSubTopics {
	BITS("channel-bits-events-v1", TwitchScopes.values()),
	SUBS("channel-subscribe-events-v1", TwitchScopes.CHANNEL_SUBSCRIPTIONS),
	COMMERCE("channel-commerce-events-v1", TwitchScopes.values()),
	WHISPERS("whispers", TwitchScopes.CHAT_LOGIN);

	private final String topic;
	private final TwitchScopes[] scope;

	PubSubTopics(String topic, TwitchScopes... scope) {
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

	public boolean isInRequiredScope(TwitchScopes... scope) {
		if (name().equalsIgnoreCase("subs") || name().equalsIgnoreCase("whispers")) {
			return Arrays.stream(scope).filter(this.scope::equals).collect(Collectors.toList()).isEmpty();
		} else return (scope.length > 0);
	}
}
