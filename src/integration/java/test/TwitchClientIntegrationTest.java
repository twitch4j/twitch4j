package me.philippheuer.twitch4j.test;

import junit.framework.TestCase;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.experimental.categories.Category;

@Category(IntegrationTestCategory.class)
abstract public class TwitchClientIntegrationTest extends TestCase {

	/**
	 * Twitch Client Instance
	 */
	public static TwitchClient twitchClient;

	/**
	 * Class Constructor
	 */
	public TwitchClientIntegrationTest() {
		// Initalize the Client a single time
		if(twitchClient == null) {
			twitchClient = TwitchClient.builder()
					.clientId("jzkbprff40iqj646a697cyrvl0zt2m6")
					.clientSecret("**SECRET**")
					.build();
		}
	}

}
