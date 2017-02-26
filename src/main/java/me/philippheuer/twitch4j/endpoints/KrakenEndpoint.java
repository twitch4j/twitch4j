package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.events.event.DonationEvent;
import me.philippheuer.twitch4j.events.event.FollowEvent;
import me.philippheuer.twitch4j.exceptions.ChannelCredentialMissingException;
import me.philippheuer.twitch4j.exceptions.ChannelDoesNotExistException;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.helper.HeaderRequestInterceptor;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.model.*;
import me.philippheuer.twitch4j.streamlabs.endpoints.DonationEndpoint;
import me.philippheuer.twitch4j.streamlabs.model.Donation;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Getter
@Setter
public class KrakenEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Constructor - by ChannelId
	 *
	 * @param client todo
	 */
	public KrakenEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint: Get OAuth Token Information
	 * Gets information about the provided oAuthToken
	 * Requires Scope: none
	 *
	 * @return todo
	 */
	public Token getToken(OAuthCredential credential) {
		// Endpoint
		String requestUrl = String.format("%s", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// REST Request
		try {
			TokenResponse responseObject = restTemplate.getForObject(requestUrl, TokenResponse.class);

			return responseObject.getToken();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Default Response: Invalid Token
		return new Token();
	}

}
