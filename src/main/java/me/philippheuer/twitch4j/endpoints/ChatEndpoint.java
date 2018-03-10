package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.ChatRoomList;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;

import java.util.Optional;

public class ChatEndpoint extends AbstractTwitchEndpoint {

	/**
	 * AbstractTwitchEndpoint
	 *
	 * @param twitchClient todo
	 */
	public ChatEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
	}

	/**
	 * Gets a Channel's Chat Rooms given the Channel ID.
	 *
	 * @param channelId todo
	 * @return todo
	 */
	public Optional<ChatRoomList> getChatRoomsByChannel(Long channelId) {
		System.out.println("I'm here.");

		// Validate Arguments
		Assert.notNull(channelId, "Please provide a channel ID!");

		// Endpoint
		String requestUrl = String.format("%s/chat/%s/rooms", Endpoints.API.getURL(), channelId);

		// REST Request
		try {
			if (!restObjectCache.containsKey(requestUrl)) {
				Logger.trace(this, "Rest Request to [%s]", requestUrl);
				ChatRoomList responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(requestUrl, ChatRoomList.class);
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

		System.out.println("Empty!!");
		return Optional.empty();
	}
}
