package me.philippheuer.twitch4j.test;

import junit.framework.TestCase;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.TwitchClientBuilder;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.experimental.categories.Category;

@Category(IntegrationTestCategory.class)
abstract public class TwitchClientIntegrationTest extends TestCase {

	/**
	 * Twitch Client Instance
	 */
	public static TwitchClient twitchClient;

	/**
	 * Test User Id
	 */
	protected static Long USER_ID = 149223493l;

	/**
	 * Test Channel Id
	 */
	protected static Long CHANNEL_ID = 149223493l;

	/**
	 * Test User Name
	 */
	protected static String USER_NAME = "twitch4j";

	/**
	 * OAuth Token
	 */
	protected static String USER_OAUTH_TOKEN = System.getProperty("OAUTH_TOKEN", "");

	/**
	 * Class Constructor
	 */
	public TwitchClientIntegrationTest() {
		// A OAuth Token


		// Initalize the Client a single time
		if(twitchClient == null) {
			twitchClient = TwitchClientBuilder.init()
					.withClientId("jzkbprff40iqj646a697cyrvl0zt2m6")
					.withClientSecret("**SECRET**")
					.build();
		}
	}

}
