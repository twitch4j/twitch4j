package me.philippheuer.twitch4j.test;

import com.sun.media.jfxmedia.logging.Logger;
import junit.framework.TestCase;
import me.philippheuer.twitch4j.TwitchClient;

abstract public class TwitchClientTestCase extends TestCase {

	/**
	 * Twitch Client Instance
	 */
	public static TwitchClient twitchClient;

	/**
	 * Class Constructor
	 */
	public TwitchClientTestCase() {
		// Initalize the Client a single time
		if(twitchClient == null) {
			twitchClient = TwitchClient.builder()
					.clientId("jzkbprff40iqj646a697cyrvl0zt2m6")
					.clientSecret("**SECRET**")
					.build();
		}
	}

}
