package twitch4j.api.kraken.endpoints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.enums.SortBy;
import twitch4j.api.kraken.exceptions.ChannelCredentialMissingException;
import twitch4j.api.kraken.exceptions.ScopeMissingException;
import twitch4j.api.kraken.json.Block;
import twitch4j.api.kraken.json.BlockList;
import twitch4j.api.kraken.json.Emote;
import twitch4j.api.kraken.json.EmoteSets;
import twitch4j.api.kraken.json.Follow;
import twitch4j.api.kraken.json.FollowList;
import twitch4j.api.kraken.json.User;
import twitch4j.api.kraken.json.UserList;
import twitch4j.api.kraken.json.UserSubscriptionCheck;
import twitch4j.api.util.rest.HeaderRequestInterceptor;
import twitch4j.api.util.rest.QueryRequestInterceptor;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;
import twitch4j.common.enums.Sort;

@Slf4j
public class UserEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get UserEndpoint
	 *
	 * @param restTemplate The Twitch Client.
	 */
	public UserEndpoint(RestTemplate restTemplate) {
		super(restTemplate);
	}

	/**
	 * Endpoint to get the UserId from the UserName
	 * <p>
	 * https://api.twitch.tv/kraken/users?login=USERNAME
	 *
	 * @param userName todo
	 * @return todo
	 */
	public Long getUserIdByUserName(String userName) {
		return getUserByUserName(userName).getId();
	}

	/**
	 * Helper to get the User Object by Name
	 *
	 * @param userName todo
	 * @return todo
	 */
	public User getUserByUserName(String userName) {
		// Validate Arguments
		Assert.hasLength(userName, "Please provide a Username!");


		String requestUrl = String.format("/users?login=%s", userName);


		// REST Request
		if (!restObjectCache.containsKey(requestUrl)) {
			try {
				UserList responseObject = restTemplate.getForObject(requestUrl, UserList.class);
				restObjectCache.put(requestUrl, responseObject, 15, TimeUnit.MINUTES);
			} catch (Exception ex) {
				log.error("Request failed: " + ex.getMessage());
				log.trace(ExceptionUtils.getStackTrace(ex));
			}
		}

		return ((UserList) restObjectCache.get(requestUrl)).getUsers().get(0);
	}

	/**
	 * Endpoint to get Privileged User Information
	 *
	 * @param credential todo
	 * @return todo
	 */
	public User getUser(ICredential credential) {
		// Validate Arguments
		Assert.notNull(credential, "Please provide Twitch Credentials!");

		// Endpoint
		RestTemplate restTemplate = this.restTemplate;

		restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.accessToken()));

		// REST Request
		try {
			return restTemplate.getForObject("/user", User.class);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return null;
		}
	}

	/**
	 * Endpoint to get User Information
	 *
	 * @param userId todo
	 * @return todo
	 */
	public User getUser(Long userId) {
		// Validate Arguments
		Assert.notNull(userId, "Please provide a User ID!");

		// Endpoint
		String requestUrl = String.format("%s/users/%d", userId);

		// REST Request
		try {
			if (!restObjectCache.containsKey(requestUrl)) {
				User responseObject = this.restTemplate.getForObject(requestUrl, User.class);
				restObjectCache.put(requestUrl, responseObject);
			}
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
		}

		return (User) restObjectCache.get(requestUrl);
	}

	/**
	 * Endpoint: Get User Emotes
	 * Gets a list of the emojis and emoticons that the specified user can use in chat. These are both the globally
	 * available ones and the channel-specific ones (which can be accessed by any user subscribed to the channel).
	 * Requires Scope: user_subscriptions
	 *
	 * @param credential UserId of the user.
	 * @return todo
	 */
	public List<Emote> getUserEmotes(ICredential credential) {
		try {
			checkScopePermission(credential.scopes(), Scope.USER_SUBSCRIPTIONS);
			// Endpoint
			String requestUrl = String.format("/users/%s/emotes", credential.userId());
			RestTemplate restTemplate = this.restTemplate;

			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.accessToken()));

			EmoteSets responseObject = restTemplate.getForObject(requestUrl, EmoteSets.class);

			List<Emote> emoteList = new ArrayList<>();
			for (List<Emote> emotes : responseObject.getEmoticonSets().values()) {
				emoteList.addAll(emotes);
			}

			return emoteList;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}

	/**
	 * Endpoint: Check User Subscription by Channel
	 * Checks if a specified user is subscribed to a specified channel.
	 * Requires Scope: user_subscriptions
	 *
	 * @param credential UserId of the user.
	 * @param channelId  ChannelId of the channel you are checking against.
	 * @return Optional of Type UserSubscriptionCheck. Is only present, when the user is subscribed.
	 */
	public Optional<UserSubscriptionCheck> getUserSubcriptionCheck(ICredential credential, Long channelId) {
		try {
			checkScopePermission(credential.scopes(), Scope.USER_SUBSCRIPTIONS);
			// Endpoint
			String requestUrl = String.format("/users/%s/subscriptions/%s", credential.userId(), channelId);
			RestTemplate restTemplate = this.restTemplate;

			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.accessToken()));

			UserSubscriptionCheck responseObject = restTemplate.getForObject(requestUrl, UserSubscriptionCheck.class);

			return Optional.ofNullable(responseObject);
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Optional.empty();
		}
	}

	/**
	 * Endpoint: Check User Subscription by Channel
	 * Checks if a specified user is subscribed to a specified channel.
	 * Requires Scope: none
	 *
	 * @param userId    UserID as Long
	 * @param limit     Maximum number of most-recent objects to return (users who started following the channel most recently). Default: 25. Maximum: 100.
	 * @param offset    Tells the server where to start fetching the next set of results, in a multi-page response.
	 * @param direction Direction of sorting. Valid values: asc (oldest first), desc (newest first). Default: desc.
	 * @param sortBy    Sorting key. Valid values: created_at, last_broadcast, login. Default: created_at.
	 * @return List of Type Follow. A list of all Follows
	 */
	public List<Follow> getUserFollows(Long userId, @Nullable Integer limit, @Nullable Integer offset, @Nullable Sort direction, SortBy sortBy) {
		// Endpoint
		String requestUrl = String.format("/users/%s/follows/channels", userId);
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (offset != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
		}
		if (direction != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("direction", direction.name().toLowerCase()));
		}
		if (sortBy != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("sortby", sortBy.name().toLowerCase()));
		}
		// REST Request
		try {
			FollowList responseObject = restTemplate.getForObject(requestUrl, FollowList.class);

			// Prepare List
			List<Follow> followList = new ArrayList<>();
			followList.addAll(responseObject.getFollows());

			// Provide User to Follow Object
			for (Follow follow : followList) {
				// The user id exists for sure, or the rest request would fail, so we can directly get the user
				follow.setUser(getUser(userId));
			}

			return followList;
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}

	/**
	 * Endpoint: Check User Follows by Channel
	 * Checks if a specified user follows a specified channel. If the user is following the channel, a follow object is returned.
	 * Requires Scope: none
	 *
	 * @param userId    UserID as Long
	 * @param channelId ChannelID as Long
	 * @return Optional Follow, if user is following.
	 */
	public Optional<Follow> checkUserFollowByChannel(Long userId, Long channelId) {
		// Endpoint
		String requestUrl = String.format("/users/%s/follows/channels/%s", userId, channelId);

		// REST Request
		try {
			Follow responseObject = restTemplate.getForObject(requestUrl, Follow.class);

			return Optional.ofNullable(responseObject);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Optional.empty();
		}

	}

	/**
	 * Endpoint: Follow Channel
	 * Adds a specified user to the followers of a specified channel.
	 * Requires Scope: user_follows_edit
	 *
	 * @param credential    Credential
	 * @param channelId     Channel to follow
	 * @param notifications Send's email notifications on true.
	 * @return Optional Follow, if user is following.
	 */
	public Boolean followChannel(ICredential credential, Long channelId, @Nullable Boolean notifications) {
		try {
			checkScopePermission(credential.scopes(), Scope.USER_FOLLOWS_EDIT);
			// Endpoint
			String requestUrl = String.format("/users/%s/follows/channels/%s", credential.userId(), channelId);
			RestTemplate restTemplate = this.restTemplate;

			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.accessToken()));

			// REST Request
			restTemplate.put(requestUrl, Follow.class, new HashMap<String, String>());

			return true;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return false;
		}
	}

	/**
	 * Endpoint: Unfollow Channel
	 * Deletes a specified user from the followers of a specified channel.
	 * Requires Scope: user_follows_edit
	 *
	 * @param credential Credential
	 * @param channelId  Channel to follow
	 * @return Optional Follow, if user is following.
	 */
	public Boolean unfollowChannel(ICredential credential, Long channelId) {
		try {
			checkScopePermission(credential.scopes(), Scope.USER_FOLLOWS_EDIT);
			// Endpoint
			String requestUrl = String.format("/users/%s/follows/channels/%s", credential.userId(), channelId);
			RestTemplate restTemplate = this.restTemplate;

			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.accessToken()));

			restTemplate.delete(requestUrl);

			return true;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return false;
		}
	}

	/**
	 * Endpoint: Get User Block List
	 * Gets a user’s block list. List sorted by recency, newest first.
	 * Requires Scope: user_blocks_read
	 *
	 * @param credential Credential to use.
	 * @param limit      Maximum number of most-recent objects to return (users who started following the channel most recently). Default: 25. Maximum: 100.
	 * @param offset     Tells the server where to start fetching the next set of results, in a multi-page response.
	 * @return todo
	 */
	public List<Block> getUserBlockList(ICredential credential, @Nullable Integer limit, @Nullable Integer offset) {
		try {
			checkScopePermission(credential.scopes(), Scope.USER_BLOCKS_READ);

			// Endpoint
			String requestUrl = String.format("/users/%s/blocks", credential.userId());
			RestTemplate restTemplate = this.restTemplate;

			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.accessToken()));

			// Parameters
			if (limit != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
			}
			if (offset != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
			}

			// REST Request
			BlockList responseObject = restTemplate.getForObject(requestUrl, BlockList.class);

			return responseObject.getBlocks();
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return Collections.emptyList();
		}
	}

	/**
	 * Endpoint: Block User
	 * Blocks a user; that is, adds a specified target user to the blocks list of a specified source user.
	 * Requires Scope: user_blocks_edit
	 *
	 * @param credential Credential
	 * @param user       Target user
	 * @return todo
	 */
	public Boolean blockUser(ICredential credential, User user) {
		try {
			checkScopePermission(credential.scopes(), Scope.USER_BLOCKS_EDIT);

			// Endpoint
			String requestUrl = String.format("/users/%s/blocks/%s", credential.userId(), user.getId());
			RestTemplate restTemplate = this.restTemplate;

			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.accessToken()));

			// REST Request
			restTemplate.put(requestUrl, null, Collections.emptyMap());
			return true;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return false;
		}
	}

	/**
	 * Endpoint: Unblock User
	 * Unblocks a user; that is, deletes a specified target user from the blocks list of a specified source user.
	 * Requires Scope: user_blocks_edit
	 *
	 * @param credential Credential
	 * @param user       Target user
	 * @return todo
	 */
	public Boolean unblockUser(ICredential credential, User user) {
		try {
			checkScopePermission(credential.scopes(), Scope.USER_BLOCKS_EDIT);

			// Endpoint
			String requestUrl = String.format("/users/%s/blocks/%s", credential.userId(), user.getId());
			RestTemplate restTemplate = this.restTemplate;

			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", "OAuth " + credential.accessToken()));

			// REST Request
			restTemplate.delete(requestUrl);
			return true;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));

			return false;
		}
	}
}
