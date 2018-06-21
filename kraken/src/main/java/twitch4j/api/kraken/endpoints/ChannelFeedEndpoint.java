package twitch4j.api.kraken.endpoints;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.exceptions.ChannelCredentialMissingException;
import twitch4j.api.kraken.exceptions.ScopeMissingException;
import twitch4j.api.kraken.model.ChannelFeed;
import twitch4j.api.kraken.model.ChannelFeedPost;
import twitch4j.api.util.rest.QueryRequestInterceptor;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;

@Slf4j
public class ChannelFeedEndpoint extends AbstractTwitchEndpoint {

	/**
	 * The Channel Feed Endpoint
	 *
	 * @param restTemplate Rest Template
	 */

	public ChannelFeedEndpoint(RestTemplate restTemplate) {
		super(restTemplate);
	}

	/**
	 * Gets posts from a specified channel feed.
	 *
	 * @param channelId The channel id, which the posts should be retrieved from.
	 * @param limit     Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
	 * @param cursor    Tells the server where to start fetching the next set of results in a multi-page response.
	 * @param comments  Specifies the number of most-recent comments on posts that are included in the response. Default: 5. Maximum: 5.
	 * @return posts from a specified channel feed.
	 */
	public List<ChannelFeedPost> getFeedPosts(Long channelId, @Nullable Integer limit, @Nullable String cursor, @Nullable Integer comments) {
		// Endpoint
		String requestUrl = String.format("/feed/%s/posts", channelId);
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.toString()));
		}
		if (cursor != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("cursor", cursor));
		}
		if (comments != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("comments", comments.toString()));
		}

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, ChannelFeed.class).getPosts();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
		}

		return Collections.emptyList();
	}

	/**
	 * Gets a specified post from a specified channel feed.
	 *
	 * @param channelId The channel id, which the posts should be retrieved from.
	 * @param postId    The post id.
	 * @param limit     Specifies the number of most-recent comments on posts that are included in the response. Default: 5. Maximum: 5.
	 * @return a specified post from a specified channel feed.
	 */
	public ChannelFeedPost getFeedPost(Long channelId, String postId, Integer limit) {
		// Endpoint
		String requestUrl = String.format("/feed/%s/posts/%s", channelId, postId);
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("comments", limit.toString()));
		}

		// REST Request
		try {
			return restTemplate.getForObject(requestUrl, ChannelFeedPost.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
		}

		return null;
	}

	/**
	 * Create Feed Post
	 * <p>
	 * Requires the Twitch *channel_feed_edit* Scope.
	 *
	 * @param credential OAuth token for a Twitch user (that as 2fa enabled)
	 * @param message    message to feed
	 * @param share      Share to Twitter if is connected
	 */
	public void createFeedPost(ICredential credential, String message, Boolean share) {
		try {
			checkScopePermission(credential.scopes(), Scope.CHANNEL_FEED_EDIT);

			// Endpoint
			String requestUrl = String.format("/feed/%s/posts", credential.userId());
			RestTemplate restTemplate = this.restTemplate;

			// Parameters
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("share", share.toString()));

			// Post Data
			MultiValueMap<String, Object> postBody = new LinkedMultiValueMap<String, Object>();
			postBody.add("content", message);

			restTemplate.postForObject(requestUrl, postBody, Void.class);

		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
		}
	}

}
