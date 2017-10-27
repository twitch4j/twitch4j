package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.model.Token;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTestCategory.class)
public class KrakenEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Integration Test: Get a invalid Token
	 * <p>
	 * Requesting an invalid token needs to return the model with valid set to false.
	 */
	@Test
	public void testGetTokenInvalid() {
		OAuthCredential credential = new OAuthCredential("INVALID_TOKEN");
		Token token = twitchClient.getKrakenEndpoint().getToken(credential);

		// Result
		assertTrue(token.getValid() == false);
	}

	/**
	 * Integration Test: Get a valid Token
	 * <p>
	 * Requesting an invalid token needs to return the model with valid set to false.
	 */
	@Test
	public void testGetTokenValid() {
		OAuthCredential credential = new OAuthCredential("INVALID_TOKEN");
		Token token = twitchClient.getKrakenEndpoint().getToken(credential);

		// Result
		/*
		assertTrue(token.getValid());
		assertNotNull(token.getUserId());
		assertNotNull(token.getUserName());
		assertNotNull(token.getClientId());
		assertNotNull(token.getAuthorization());
		*/
	}

}
