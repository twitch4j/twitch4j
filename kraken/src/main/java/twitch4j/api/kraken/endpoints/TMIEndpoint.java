package twitch4j.api.kraken.endpoints;

import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import twitch4j.api.kraken.json.Channel;
import twitch4j.api.kraken.json.User;
import twitch4j.api.kraken.json.tmi.Chatter;
import twitch4j.api.kraken.json.tmi.ChatterResult;

import java.util.concurrent.TimeUnit;

/**
 * Twitch Messaging Interface (TMI)
 * This Endpoint can be changed at any time. (Unofficial)
 */
@Slf4j
public class TMIEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Twitch Messaging Interface (TMI)
	 *
	 * @param restTemplate {@link RestTemplate}
	 */
	public TMIEndpoint(RestTemplate restTemplate) {
		super(new RestTemplate(new OkHttp3ClientHttpRequestFactory()));
		this.restTemplate.setInterceptors(restTemplate.getInterceptors());
		this.restTemplate.setMessageConverters(restTemplate.getMessageConverters());
		this.restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://tmi.twitch.tv"));
	}

	/**
	 * Gets all user's present in the twitch chat of a channel.
	 *
	 * @param channelName Channel to fetch the information for.
	 * @return All chatters in a channel, separated into groups like admins, moderators and viewers.
	 */
	public Chatter getChatters(String channelName) {
		// Endpoint
		String requestUrl = String.format("/group/user/%s/chatters", channelName);

		// REST Request
		try {
			if (!restObjectCache.containsKey(requestUrl)) {
				ChatterResult responseObject = restTemplate.getForObject(requestUrl, ChatterResult.class);
				restObjectCache.put(requestUrl, responseObject, ExpirationPolicy.CREATED, 60, TimeUnit.SECONDS);
			}

			return ((ChatterResult) restObjectCache.get(requestUrl)).getChatters();

		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
		}

		// OnError: Return empty result
		return new Chatter();
	}

	/**
	 * Checks if a given user is moderator for a channel
	 *
	 * @param channel Channel
	 * @param user    User
	 * @return Boolean true, if the user is moderator.
	 */
	public Boolean isUserModerator(Channel channel, User user) {
		Chatter chatter = getChatters(channel.getName());

		return chatter.getModerators().contains(user.getName());
	}
}
