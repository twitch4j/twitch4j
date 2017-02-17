package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.exceptions.ChannelCredentialMissingException;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.helper.QueryRequestInterceptor;
import me.philippheuer.twitch4j.model.*;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class UserEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Get UserEndpoint
	 */
	public UserEndpoint(TwitchClient client) {
		super(client);
	}

	/**
	 * Endpoint to get the UserId from the UserName
	 * <p>
	 * https://api.twitch.tv/kraken/users?login=USERNAME
	 */
	public Optional<Long> getUserIdByUserName(String userName) {
		// Validate Arguments
		Assert.hasLength(userName, "Please provide a Username!");

		// REST Request
		String requestUrl = String.format("%s/users?login=%s", getTwitchClient().getTwitchEndpoint(), userName);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		if (!restObjectCache.containsKey(requestUrl)) {
			UserList responseObject = restTemplate.getForObject(requestUrl, UserList.class);
			restObjectCache.put(requestUrl, responseObject, 15, TimeUnit.MINUTES);
		}

		List<User> userList = ((UserList) restObjectCache.get(requestUrl)).getUsers();

		// User found?
		if (userList.size() == 1) {
			return Optional.ofNullable(userList.get(0).getId());
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Helper to get the User Object by Name
	 */
	public Optional<User> getUserByUserName(String userName) {
		// Validate Arguments
		Assert.hasLength(userName, "Please provide a Username!");

		Optional<Long> userId = getUserIdByUserName(userName);
		if (userId.isPresent()) {
			return getUser(userId.get());
		}

		return Optional.empty();
	}

	/**
	 * Endpoint to get Privileged User Information
	 */
	public Optional<User> getUser(OAuthCredential OAuthCredential) {
		// Validate Arguments
		Assert.notNull(OAuthCredential, "Please provide Twitch Credentials!");

		// Endpoint
		String requestUrl = String.format("%s/user", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(OAuthCredential);

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			User responseObject = restTemplate.getForObject(requestUrl, User.class);

			return Optional.ofNullable(responseObject);
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	/**
	 * Endpoint to get User Information
	 */
	public Optional<User> getUser(Long userId) {
		// Validate Arguments
		Assert.notNull(userId, "Please provide a User ID!");

		// Endpoint
		String requestUrl = String.format("%s/users/%d", getTwitchClient().getTwitchEndpoint(), userId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			if (!restObjectCache.containsKey(requestUrl)) {
				Logger.trace(this, "Rest Request to [%s]", requestUrl);
				User responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(requestUrl, User.class);
				restObjectCache.put(requestUrl, responseObject);
			}

			return Optional.ofNullable((User) restObjectCache.get(requestUrl));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	/**
	 * Endpoint: Get User Emotes
	 * Gets a list of the emojis and emoticons that the specified user can use in chat. These are both the globally
	 * available ones and the channel-specific ones (which can be accessed by any user subscribed to the channel).
	 * Requires Scope: user_subscriptions
	 *
	 * @param userId UserId of the user.
	 */
	public List<Emote> getUserEmotes(Long userId) {
		// Check Scope
		Optional<OAuthCredential> credential = getTwitchClient().getCredentialManager().getTwitchCredentialsForChannel(userId);
		if (credential.isPresent()) {
			Set<String> requiredScopes = new HashSet<String>();
			requiredScopes.add(TwitchScopes.USER_SUBSCRIPTIONS.getKey());

			checkScopePermission(credential.get().getOAuthScopes(), requiredScopes);
		} else {
			throw new ChannelCredentialMissingException(userId);
		}

		// Endpoint
		String requestUrl = String.format("%s/users/%s/emotes", getTwitchClient().getTwitchEndpoint(), userId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential.get());

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			EmoteSets responseObject = restTemplate.getForObject(requestUrl, EmoteSets.class);

			List<Emote> emoteList = new ArrayList<>();

			for (List<Emote> emotes : responseObject.getEmoticonSets().values()) {
				emoteList.addAll(emotes);
			}

			return emoteList;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ArrayList<Emote>();
	}

	/**
	 * Endpoint: Check User Subscription by Channel
	 * Checks if a specified user is subscribed to a specified channel.
	 * Requires Scope: user_subscriptions
	 *
	 * @param userId UserId of the user.
	 * @return Optional<UserSubscriptionCheck> Is only present, when the user is subscribed.
	 */
	public Optional<UserSubscriptionCheck> getUserSubcriptionCheck(Long userId, Long channelId) {
		// Endpoint
		String requestUrl = String.format("%s/users/%s/subscriptions/%s", getTwitchClient().getTwitchEndpoint(), userId, channelId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			UserSubscriptionCheck responseObject = restTemplate.getForObject(requestUrl, UserSubscriptionCheck.class);

			return Optional.ofNullable(responseObject);
		} catch (RestException restException) {
			if (restException.getRestError().getStatus().equals(422)) {
				// Channel has no subscription program.
				Logger.info(this, "Channel %s has no subscription programm.", channelId);
			} else {
				Logger.error(this, "RestException: " + restException.getRestError().toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return Optional.empty();
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
	 * @return List<Follow> A list of all Follows
	 */
	public List<Follow> getUserFollows(Long userId, Optional<Long> limit, Optional<Long> offset, Optional<String> direction, Optional<String> sortBy) {
		// Endpoint
		String requestUrl = String.format("%s/users/%s/follows/channels", getTwitchClient().getTwitchEndpoint(), userId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("direction", direction.orElse("desc")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("sortby", sortBy.orElse("created_at")));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			FollowList responseObject = restTemplate.getForObject(requestUrl, FollowList.class);

			// Prepare List
			List<Follow> followList = new ArrayList<>();
			followList.addAll(responseObject.getFollows());

			// Provide User to Follow Object
			for (Follow follow : followList) {
				// The user id exists for sure, or the rest request would fail, so we can directly get the user
				follow.setUser(getUser(userId).get());
			}

			return followList;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ArrayList<Follow>();
	}

	/**
	 * Endpoint: Check User Follows by Channel
	 * Checks if a specified user follows a specified channel. If the user is following the channel, a follow object is returned.
	 * Requires Scope: none
	 *
	 * @param userId    UserID as Long
	 * @param channelId ChannelID as Long
	 * @return Optional<Follow> Follow, if user is following.
	 */
	public Optional<Follow> checkUserFollowByChannel(Long userId, Long channelId) {
		// Endpoint
		String requestUrl = String.format("%s/users/%s/follows/channels/%s", getTwitchClient().getTwitchEndpoint(), userId, channelId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			Follow responseObject = restTemplate.getForObject(requestUrl, Follow.class);

			return Optional.ofNullable(responseObject);
		} catch (RestException restException) {
			if (restException.getRestError().getStatus().equals(404)) {
				// User does not follow channel
				return Optional.empty();
			} else {
				Logger.error(this, "RestException: " + restException.getRestError().toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return Optional.empty();
	}

	/**
	 * Endpoint: Follow Channel
	 * Adds a specified user to the followers of a specified channel.
	 * Requires Scope: user_follows_edit
	 *
	 * @param credential    Credential
	 * @param channelId     Channel to follow
	 * @param notifications Send's email notifications on true.
	 * @return Optional<Follow> Follow, if user is following.
	 */
	public Boolean followChannel(OAuthCredential credential, Long channelId, Optional<Boolean> notifications) {
		// Endpoint
		String requestUrl = String.format("%s/users/%s/follows/channels/%s", getTwitchClient().getTwitchEndpoint(), credential.getUserId(), channelId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			restTemplate.put(requestUrl, Follow.class, new HashMap<String, String>());

			return true;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * Endpoint: Unfollow Channel
	 * Deletes a specified user from the followers of a specified channel.
	 * Requires Scope: user_follows_edit
	 *
	 * @param credential Credential
	 * @param channelId  Channel to follow
	 * @return Optional<Follow> Follow, if user is following.
	 */
	public Boolean unfollowChannel(OAuthCredential credential, Long channelId) {
		// Endpoint
		String requestUrl = String.format("%s/users/%s/follows/channels/%s", getTwitchClient().getTwitchEndpoint(), credential.getUserId(), channelId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			restTemplate.delete(requestUrl);

			return true;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * Endpoint: Get User Block List
	 * Gets a user’s block list. List sorted by recency, newest first.
	 * Requires Scope: user_blocks_read
	 *
	 * @param credential Credential to use.
	 * @param limit      Maximum number of most-recent objects to return (users who started following the channel most recently). Default: 25. Maximum: 100.
	 * @param offset     Tells the server where to start fetching the next set of results, in a multi-page response.
	 */
	public List<Block> getUserBlockList(OAuthCredential credential, Optional<Long> limit, Optional<Long> offset) {
		// Endpoint
		String requestUrl = String.format("%s/users/%s/blocks", getTwitchClient().getTwitchEndpoint(), credential.getUserId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			BlockList responseObject = restTemplate.getForObject(requestUrl, BlockList.class);

			return responseObject.getBlocks();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ArrayList<Block>();
	}

	/**
	 * Endpoint: Block User
	 * Blocks a user; that is, adds a specified target user to the blocks list of a specified source user.
	 * Requires Scope: user_blocks_edit
	 *
	 * @param credential   Credential
	 * @param targetUserId UserID of the Target
	 */
	public Boolean addBlock(OAuthCredential credential, Long targetUserId) {
		// Endpoint
		String requestUrl = String.format("%s/users/%s/blocks/%s", getTwitchClient().getTwitchEndpoint(), credential.getUserId(), targetUserId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			restTemplate.put(requestUrl, Follow.class, new HashMap<String, String>());

			return true;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * Endpoint: Unblock User
	 * Unblocks a user; that is, deletes a specified target user from the blocks list of a specified source user.
	 * Requires Scope: user_blocks_edit
	 *
	 * @param credential   Credential
	 * @param targetUserId UserID of the Target
	 */
	public Boolean deleteBlock(OAuthCredential credential, Long targetUserId) {
		// Endpoint
		String requestUrl = String.format("%s/users/%s/blocks/%s", getTwitchClient().getTwitchEndpoint(), credential.getUserId(), targetUserId);
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			restTemplate.delete(requestUrl);

			return true;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

}
