package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;
import me.philippheuer.twitch4j.model.tmi.Chatter;
import me.philippheuer.twitch4j.model.tmi.ChatterResult;
import net.jodah.expiringmap.ExpirationPolicy;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Twitch Messaging Interface (TMI)
 * This Endpoint can be changed at any time. (Unofficial)
 */
public class TMIEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Twitch Messaging Interface (TMI)
	 *
	 * @param client todo
	 */
	public TMIEndpoint(TwitchClient client) {
		super(client);
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
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			if (!restObjectCache.containsKey(requestUrl)) {
				Logger.trace(this, "Rest Request to [%s]", requestUrl);
				ChatterResult responseObject = restTemplate.getForObject(requestUrl, ChatterResult.class);
				restObjectCache.put(requestUrl, responseObject, ExpirationPolicy.CREATED, 60, TimeUnit.SECONDS);
			}

			return ((ChatterResult) restObjectCache.get(requestUrl)).getChatters();

		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
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
