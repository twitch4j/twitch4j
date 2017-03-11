package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.ChannelFeed;
import me.philippheuer.twitch4j.model.ChannelFeedPost;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ChannelFeedEndpoint extends AbstractTwitchEndpoint {

	/**
	 * The Channel Feed Endpoint
	 *
	 * @param twitchClient The Twitch Client.
	 */
	public ChannelFeedEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
	}

	/**
	 * Gets posts from a specified channel feed.
	 *
	 * @param channelId    The channel id, which the posts should be retrieved from.
	 * @param limit        Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
	 * @param cursor       Tells the server where to start fetching the next set of results in a multi-page response.
	 * @param commentLimit Specifies the number of most-recent comments on posts that are included in the response. Default: 5. Maximum: 5.
	 * @return posts from a specified channel feed.
	 */
	public List<ChannelFeedPost> getFeedPosts(Long channelId, Optional<Long> limit, Optional<String> cursor, Optional<Long> commentLimit) {
		// Endpoint
		String requestUrl = String.format("%s/feed/%s/posts", getTwitchClient().getTwitchEndpoint(), channelId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(10l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("cursor", cursor.orElse("")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("comments", commentLimit.orElse(5l).toString()));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			ChannelFeed responseObject = restTemplate.getForObject(requestUrl, ChannelFeed.class);

			return responseObject.getPosts();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ArrayList<ChannelFeedPost>();
	}

	/**
	 * Gets a specified post from a specified channel feed.
	 *
	 * @param channelId    The channel id, which the posts should be retrieved from.
	 * @param postId       The post id.
	 * @param commentLimit Specifies the number of most-recent comments on posts that are included in the response. Default: 5. Maximum: 5.
	 * @return a specified post from a specified channel feed.
	 */
	public ChannelFeedPost getFeedPost(Long channelId, String postId, Optional<Long> commentLimit) {
		// Endpoint
		String requestUrl = String.format("%s/feed/%s/posts/%s", getTwitchClient().getTwitchEndpoint(), channelId, postId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("comments", commentLimit.orElse(5l).toString()));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			ChannelFeedPost responseObject = restTemplate.getForObject(requestUrl, ChannelFeedPost.class);

			return responseObject;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

}
