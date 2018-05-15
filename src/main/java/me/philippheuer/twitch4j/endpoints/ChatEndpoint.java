package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.ChatRoomList;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class ChatEndpoint extends AbstractTwitchEndpoint {

	/**
	 * ChatEndpoint
	 *
	 * @param twitchClient todo
	 */
	public ChatEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
	}

	/**
	 * Get a channel's chat rooms based from the authenticated channel's perspective. We need the authenticated channel
	 * ID here because the API needs to know with what respect to return a channel's chat rooms from.
	 *
	 * For example, if you own the channel, you'll get all the chat rooms (subscriber only, mod only, etc.).
	 * However, if you're only a viewer of that channel, you'll only get the rooms that you can see (not sub only, etc.).
	 *
	 * @param authenticatedChannelId The channel ID that you're authenticated under.
	 * @param channelIdToQuery The channel ID that you're trying to get the chat rooms for.
	 * @return
	 */
	public Optional<ChatRoomList> getChatRoomsByChannel(Long authenticatedChannelId, Long channelIdToQuery) {
		// Validate Arguments
		Assert.notNull(authenticatedChannelId, "Please provide a channel ID that you're authenticated for!");
		Assert.notNull(channelIdToQuery, "Please provide a channel ID to query its chat rooms for!");

		// Endpoint
		String requestUrl = String.format("%s/chat/%s/rooms", Endpoints.API.getURL(), channelIdToQuery);

		// REST Request
		try {
			Optional<OAuthCredential> credential = getTwitchClient().getCredentialManager().getTwitchCredentialsForChannel(authenticatedChannelId);
			if (!credential.isPresent()) {
				throw new IllegalArgumentException("Can't get credential for channel. Did you authenticate under this channel ID with Twitch?");
			}

			RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential.get());
			if (!restObjectCache.containsKey(requestUrl)) {
				Logger.trace(this, "Rest Request to [%s]", requestUrl);
				ChatRoomList responseObject = restTemplate.getForObject(requestUrl, ChatRoomList.class);
				restObjectCache.put(requestUrl, responseObject);
			}

			return Optional.ofNullable((ChatRoomList) restObjectCache.get(requestUrl));
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
			restException.printStackTrace();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
			ex.printStackTrace();
		}

		return Optional.empty();
	}
}
