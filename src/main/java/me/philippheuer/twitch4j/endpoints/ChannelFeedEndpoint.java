package me.philippheuer.twitch4j.endpoints;

import lombok.extern.slf4j.Slf4j;
import me.philippheuer.twitch4j.TwitchClient;

@Slf4j
@Deprecated
public class ChannelFeedEndpoint extends AbstractTwitchEndpoint {

	/**
	 * The Channel Feed Endpoint
	 *
	 * @param client The Twitch Client.
	 * @deprecated Twitch removes Channel Feeds and Pulse. More info <a href="https://discuss.dev.twitch.tv/t/how-the-removal-of-channel-feed-and-pulse-affects-the-twitch-api-v5/16540">here</a>.
	 */

	public ChannelFeedEndpoint(TwitchClient client) {
		super(client, client.getRestClient().getRestTemplate());
	}

}
