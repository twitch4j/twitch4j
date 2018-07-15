package twitch4j.endpoints;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import twitch4j.TwitchClient;
import twitch4j.model.ChatRoom;
import twitch4j.model.ChatRoomList;
import twitch4j.model.Emote;
import twitch4j.model.EmoteSets;
import twitch4j.model.Emoticon;
import twitch4j.model.EmoticonList;
import twitch4j.util.rest.QueryRequestInterceptor;

@Slf4j
public class ChatEndpoint extends AbstractTwitchEndpoint {

	/**
	 * ChatEndpoint
	 *
	 * @param client The Twitch Client.
	 */
	public ChatEndpoint(TwitchClient client) {
		super(client, client.getRestClient().getRestTemplate());
	}

	/**
	 * Get a channel's chat rooms based from the authenticated channel's perspective. We need the authenticated channel
	 * ID here because the API needs to know with what respect to return a channel's chat rooms from.
	 * <p>
	 * For example, if you own the channel, you'll get all the chat rooms (subscriber only, mod only, etc.).
	 * However, if you're only a viewer of that channel, you'll only get the rooms that you can see (not sub only, etc.).
	 *
	 * @param channelId The channel ID that you're trying to get the chat rooms for.
	 * @return
	 */
	public List<ChatRoom> getChatRooms(Long channelId) {
		// Validate Arguments
		Objects.requireNonNull(channelId, "Please provide a channel ID to query its chat rooms for!");

		// Endpoint
		String requestUrl = String.format("/chat/%s/rooms", channelId);

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, ChatRoomList.class).getRooms();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return Collections.emptyList();
		}
	}

	public List<Emote> getEmoteSets(Long emoteSets) {
		// Validate Arguments
		Assert.notNull(emoteSets, "Please provide a emote sets!");
		String requestUri = "/chat/emoticon_images";
		RestTemplate restTemplate = this.restTemplate;

		restTemplate.getInterceptors().add(new QueryRequestInterceptor("emotesets", emoteSets.toString()));

		try {
			return restTemplate.getForObject(requestUri, EmoteSets.class).getEmoticonSets().get(emoteSets.toString());
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return Collections.emptyList();
		}
	}

	public List<Emoticon> getEmotes() {
		// Validate Arguments
		String requestUri = "/chat/emoticon_images";

		try {
			return restTemplate.getForObject(requestUri, EmoticonList.class).getEmoticons();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return Collections.emptyList();
		}
	}
}
