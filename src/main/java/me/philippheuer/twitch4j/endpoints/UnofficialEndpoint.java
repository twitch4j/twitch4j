package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.unofficial.AdvancedChannelInformation;
import me.philippheuer.twitch4j.model.unofficial.Ember;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
public class UnofficialEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Endpoint for all unofficial api endpoints
	 *
	 * @param twitchClient The Twitch Client.
	 */
	public UnofficialEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
	}

	/**
	 * Gets the primary team for a channel, which is displayed in the channel.
	 *
	 * @param userName Twitch username
	 * @return todo
	 */
	public Ember getEmber(String userName) {
		// Endpoint
		String requestUrl = String.format("https://api.twitch.tv/api/channels/%s/ember", userName);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			Ember responseObject = restTemplate.getForObject(requestUrl, Ember.class);

			return responseObject;
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Gets the steam profile id, if the streamer has linked his steam account.
	 *
	 * @param userName Twitch username
	 * @return todo
	 */
	public String getConnectedSteamProfile(String userName) {
		// Endpoint
		String requestUrl = String.format("https://api.twitch.tv/api/channels/%s", userName);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			AdvancedChannelInformation responseObject = restTemplate.getForObject(requestUrl, AdvancedChannelInformation.class);

			return responseObject.getSteamId();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}
}
