package me.philippheuer.twitch4j.streamlabs.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.util.rest.LoggingRequestInterceptor;
import me.philippheuer.twitch4j.streamlabs.StreamlabsClient;
import me.philippheuer.twitch4j.streamlabs.model.AlertCreate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
public class AlertEndpoint extends AbstractStreamlabsEndpoint {

	/**
	 * Holds the credentials to the current user
	 */
	private OAuthCredential oAuthCredential;

	/**
	 * Stream Labs - Authenticated Endpoint
	 *
	 * @param streamlabsClient todo
	 * @param credential todo
	 */
	public AlertEndpoint(StreamlabsClient streamlabsClient, OAuthCredential credential) {
		super(streamlabsClient);
		setOAuthCredential(credential);
	}

	/**
	 * Endpoint: Create Alert
	 * Trigger a custom alert for the authenticated user.
	 * Requires Scope: alerts.create
	 *
	 * @param type               This parameter determines which alert box this alert will show up in, and thus should be one of the following: follow, subscription, donation, or host
	 * @param message            The message to show with this alert. If not supplied, no message will be shown. Surround special tokens with *s, for example: This is my *special* alert!
	 * @param duration           How many seconds this alert should be displayed.
	 * @param special_text_color The color to use for special tokens. Must be a valid CSS color string.
	 * @param imageUrl           The href pointing to an image resource to play when this alert shows. If an empty string is supplied, no image will be displayed.
	 * @param soundUrl           The href pointing to a sound resource to play when this alert shows. If an empty string is supplied, no sound will be played.
	 * @return Success?
	 */
	public Boolean createAlert(String type, Optional<String> message, Optional<Integer> duration, Optional<String> special_text_color, Optional<String> imageUrl, Optional<String> soundUrl) {
		// Validate Parameters
		if (!Arrays.asList("follow", "subscription", "donation", "host").contains(type)) {
			throw new RuntimeException("Invalid Type");
		}

		// Endpoint
		String requestUrl = String.format("%s/alerts", getStreamlabsClient().getEndpointUrl());
		RestTemplate restTemplate = getStreamlabsClient().getRestClient().getRestTemplate();

		// Post Data
		MultiValueMap<String, Object> postBody = new LinkedMultiValueMap<String, Object>();
		postBody.add("access_token", getOAuthCredential().getOAuthToken());
		postBody.add("type", type);
		postBody.add("message", message.orElse(""));
		postBody.add("duration", duration.orElse(10).toString());
		postBody.add("special_text_color", special_text_color.orElse(""));
		postBody.add("image_href", imageUrl.orElse(""));
		postBody.add("sound_href", soundUrl.orElse(""));

		restTemplate.getInterceptors().add(new LoggingRequestInterceptor());

		// REST Request
		try {
			AlertCreate responseObject = restTemplate.postForObject(requestUrl, postBody, AlertCreate.class);

			Logger.debug(this, "Sreamlabs: Created new Alert for %s", getOAuthCredential().getDisplayName());

			return responseObject.getSuccess();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

}
