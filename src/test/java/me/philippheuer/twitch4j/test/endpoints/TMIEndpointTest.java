package me.philippheuer.twitch4j.test.endpoints;

import junit.framework.TestCase;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.tmi.Chatter;
import me.philippheuer.twitch4j.test.TwitchClientTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * These tests check the unofficial tmi endpoints.
 */
public class TMIEndpointTest extends TwitchClientTestCase {

	/**
	 * Test the getChatters Method
	 */
	@Test
	public void testGetChatters() {
		// Request
		Chatter chatter = twitchClient.getTMIEndpoint().getChatters("twitch4j");

		// Result
		assertNotNull(chatter);
		assertNotNull(chatter.getAdmins());
		assertNotNull(chatter.getGlobalMods());
		assertNotNull(chatter.getModerators());
		assertNotNull(chatter.getStaff());
		assertNotNull(chatter.getViewers());
	}

}
