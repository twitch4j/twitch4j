package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.ChannelFeed;
import me.philippheuer.twitch4j.model.ChannelFeedPost;
import me.philippheuer.twitch4j.model.Void;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
		String requestUrl = String.format("%s/feed/%s/posts", Endpoints.API.getURL(), channelId);
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
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
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
		String requestUrl = String.format("%s/feed/%s/posts/%s", Endpoints.API.getURL(), channelId, postId);
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
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Create Feed Post
	 * <p>
	 * Requires the Twitch *channel_feed_edit* Scope.
	 *
	 * @param credential  OAuth token for a Twitch user (that as 2fa enabled)
	 * @param channelId Channel ID
	 * @param message message to feed
	 * @param share Share to Twitter if is connected
	 */
	public void createFeedPost(OAuthCredential credential, Long channelId, String message, Optional<Boolean> share) {
		// Endpoint
		String requestUrl = String.format("%s//feed/%s/posts", Endpoints.API.getURL(), channelId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("share", share.orElse(false).toString()));

		// Post Data
		MultiValueMap<String, Object> postBody = new LinkedMultiValueMap<String, Object>();
		postBody.add("content", message);

		// REST Request
		try {
			restTemplate.postForObject(requestUrl, postBody, Void.class);
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}
	}

	// Requires channel_feed_edit


}
