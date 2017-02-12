package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.*;
import me.philippheuer.twitch4j.model.tmi.Chatter;
import me.philippheuer.twitch4j.model.tmi.ChatterResult;
import org.springframework.web.client.RestTemplate;

/**
 * Twitch Messaging Interface (TMI)
 * This Endpoint can't be changed at any time. (Unofficial)
 */
public class TMIEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Twitch Messaging Interface (TMI)
	 */
	public TMIEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint: Get All Teams
	 * Gets all active teams.
	 * Requires Scope: none
	 *
	 * @param channelName  Channel to fetch the information for.
	 */
	public Chatter getChatters(String channelName) {
		// Endpoint
		String requestUrl = String.format("%s/group/user/aimbotcalvin/chatters", getTwitchClient().getTwitchMessagingInterfaceEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			ChatterResult responseObject = restTemplate.getForObject(requestUrl, ChatterResult.class);

			return responseObject.getChatters();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// OnError: Return empty result
		return new Chatter();
	}

}
