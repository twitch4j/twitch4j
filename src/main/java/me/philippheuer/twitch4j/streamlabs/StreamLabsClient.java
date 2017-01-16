package me.philippheuer.twitch4j.streamlabs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.*;

import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.pubsub.TwitchPubSub;

@Getter
@Setter
public class StreamLabsClient {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(TwitchPubSub.class);

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * Constructor
	 */
	public StreamLabsClient(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);
	}
}
