package twitch4j.endpoints;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import org.springframework.web.client.RestTemplate;
import twitch4j.TwitchClient;
import twitch4j.enums.Endpoints;
import twitch4j.exceptions.RestException;
import twitch4j.model.Channel;
import twitch4j.model.User;
import twitch4j.model.tmi.Chatter;
import twitch4j.model.tmi.ChatterResult;

/**
 * Twitch Messaging Interface (TMI)
 * This Endpoint can be changed at any time. (Unofficial)
 */
@Slf4j
public class TMIEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Twitch Messaging Interface (TMI)
	 *
	 * @param client The Twitch Client.
	 */
	public TMIEndpoint(TwitchClient client) {
		super(client, client.getRestClient().getRestTemplate());
	}

	/**
	 * Gets all user's present in the twitch chat of a channel.
	 *
	 * @param channelName Channel to fetch the information for.
	 * @return All chatters in a channel, separated into groups like admins, moderators and viewers.
	 */
	public Chatter getChatters(String channelName) {
		// Endpoint
		String requestUrl = String.format("%s/group/user/%s/chatters", Endpoints.TMI.getURL(), channelName);
		RestTemplate restTemplate = client.getRestClient().getRestTemplate();

		// REST Request
		try {
			if (!restObjectCache.containsKey(requestUrl)) {
				log.trace("Rest Request to [{}]", requestUrl);
				ChatterResult responseObject = restTemplate.getForObject(requestUrl, ChatterResult.class);
				restObjectCache.put(requestUrl, responseObject, ExpirationPolicy.CREATED, 60, TimeUnit.SECONDS);
			}

			return ((ChatterResult) restObjectCache.get(requestUrl)).getChatters();

		} catch (RestException restException) {
			log.error("RestException: {}", restException.getRestError().toString());
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage(), ex);
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
