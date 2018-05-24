package twitch4j.api.kraken.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import twitch4j.api.kraken.json.unofficial.AdvancedChannelInformation;
import twitch4j.api.kraken.json.unofficial.Ember;
import twitch4j.common.utils.Unofficial;

@Slf4j
@Unofficial
public class UnofficialEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Endpoint for all unofficial api endpoints
	 *
	 * @param restTemplate The Twitch Client.
	 */
	public UnofficialEndpoint(RestTemplate restTemplate) {
		super(restTemplate);
		this.restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://api.twitch.tv/api"));
	}

	/**
	 * Gets the primary team for a channel, which is displayed in the channel.
	 *
	 * @param userName Twitch username
	 * @return todo
	 */
	@Unofficial
	public Ember getEmber(String userName) {
		// Endpoint
		String requestUrl = String.format("/channels/%s/ember", userName);

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, Ember.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}

	/**
	 * Gets the steam profile id, if the streamer has linked his steam account.
	 *
	 * @param userName Twitch username
	 * @return todo
	 */
	@Unofficial
	public String getConnectedSteamProfile(String userName) {
		// Endpoint
		String requestUrl = String.format("/channels/%s", userName);

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, AdvancedChannelInformation.class).getSteamId();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}
}
