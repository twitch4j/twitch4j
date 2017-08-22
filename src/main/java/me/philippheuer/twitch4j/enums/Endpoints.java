package me.philippheuer.twitch4j.enums;

import lombok.Getter;

/**
 * This enums data contains endpoints to Twitch Services
 * @author Damian Staszewski
 */
@Getter
public enum Endpoints {
	API("https://api.twitch.tv/kraken"), // default version api is v5
	PUBSUB("wss://pubsub-edge.twitch.tv:443"),
	IRC("wss://irc-ws.chat.twitch.tv:443"),
	TMI("http://tmi.twitch.tv");

	private final String URL;

	Endpoints(String url) {
		this.URL = url;
	}
}
