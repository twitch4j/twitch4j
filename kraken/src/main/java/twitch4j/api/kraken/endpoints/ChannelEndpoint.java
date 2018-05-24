package twitch4j.api.kraken.endpoints;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import twitch4j.api.kraken.enums.BroadcastType;
import twitch4j.api.kraken.enums.VideoSort;
import twitch4j.api.kraken.exceptions.ChannelCredentialMissingException;
import twitch4j.api.kraken.exceptions.ScopeMissingException;
import twitch4j.api.kraken.json.*;
import twitch4j.api.util.rest.HeaderRequestInterceptor;
import twitch4j.api.util.rest.QueryRequestInterceptor;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;
import twitch4j.common.enums.Sort;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ChannelEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Commercial Lengths
	 */
	private final List<Integer> validCommercialLengths = Arrays.asList(30, 60, 90, 120, 150, 180);

	/**
	 * Channel Endpoint
	 *
	 * @param restTemplate Rest Template
	 */
	public ChannelEndpoint(RestTemplate restTemplate) {
		super(restTemplate);
	}

	/**
	 * Endpoint: Get Channel
	 * Gets a specified channel object.
	 *
	 * @return todo
	 */
	public Channel getChannel(Long channelId) {
		// Endpoint
		String endpoint = String.format("/channels/%s", channelId);

		// REST Request
		try {
			if (!restObjectCache.containsKey(endpoint)) {
				Channel responseObject = restTemplate.getForObject(endpoint, Channel.class);
				restObjectCache.put(endpoint, responseObject);
			}

			return (Channel) restObjectCache.get(endpoint);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return null;
		}
	}

	/**
	 * Endpoint: Get Channel
	 * Get Channel returns more data than Get Channel by ID because Get Channel is privileged.
	 * Requires Scope: channel_read
	 *
	 * @return todo
	 */
	public Channel getChannel(ICredential credential) {
		try {
			checkScopePermission(credential.scopes(), Collections.singleton(Scope.CHANNEL_READ));

			// Endpoint
			String requestUrl = "/channel";
			RestTemplate restTemplate = this.restTemplate;

			// Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.accessToken())));

			if (!restObjectCache.containsKey(requestUrl)) {
				Channel responseObject = restTemplate.getForObject(requestUrl, Channel.class);
				restObjectCache.put(requestUrl, responseObject);
			}

			return (Channel) restObjectCache.get(requestUrl);

		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return null;
		}
	}

	/**
	 * Endpoint: Get Channel Editors
	 * Gets a list of users who are editors for a specified channel.
	 * Requires Scope: channel_read
	 *
	 * @return todo
	 */
	public List<User> getEditors(ICredential credential) {
		// Endpoint
		Channel channel = getChannel(credential);
		String requestUrl = "/channels/" + channel.getId() + "/editors";
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.accessToken())));

		// REST Request
		try {
			UserList responseObject = restTemplate.getForObject(requestUrl, UserList.class);

			return responseObject.getUsers();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return null;
		}
	}

	/**
	 * Endpoint: Get Channel Followers
	 * Gets a list of users who follow a specified channel, sorted by the date when they started following the channel (newest first, unless specified otherwise).
	 * Requires Scope: none
	 *
	 * @param limit     Maximum number of most-recent objects to return (users who started following the channel most recently). Default: 25. Maximum: 100.
	 * @param cursor    Tells the server where to start fetching the next set of results, in a multi-page response.
	 * @param direction Direction of sorting. Valid values: asc (oldest first), desc (newest first). Default: desc.
	 * @return todo
	 */
	public List<Follow> getFollowers(Long channelId, @Nullable Integer limit, @Nullable String cursor, @Nullable Sort direction) {
		// Endpoint
		String requestUrl = String.format("/channels/%s/follows",  channelId);

		// parameters
		List<String> query = new ArrayList<>();
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (cursor != null && !cursor.equals("")) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("cursor", cursor));
			query.add("cursor=" + cursor);
		}
		if (direction != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("direction", direction.name().toLowerCase()));
		}

		if (!query.isEmpty()) {
			requestUrl += "?" + String.join("&", query);
		}

		// REST Request
		try {
			FollowList responseObject = restTemplate.getForObject(requestUrl, FollowList.class);

			// Provide the Follow with info about the channel
			for(Follow f : responseObject.getFollows()) f.setChannel(getChannel(channelId));

			return responseObject.getFollows();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return Collections.emptyList();
		}
	}

	/**
	 * Endpoint: Get Channel Followers
	 * Gets a list of users who follow a specified channel, sorted by the date when they started following the channel (newest first, unless specified otherwise).
	 * Requires Scope: none
	 *
	 * @param limit     Maximum number of most-recent objects to return (users who started following the channel most recently). Default: 25. Maximum: none.
	 * @param direction Direction of sorting. Valid values: asc (oldest first), desc (newest first). Default: desc.
	 * @return todo
	 */
	public List<Follow> getFollowers(Long channelId, @Nullable Integer limit, @Nullable Sort direction) {
		return getFollowers(channelId, limit, null, direction);
	}

	/**
	 * Endpoint: Get Channel Teams
	 * Gets a list of teams to which a specified channel belongs.
	 * Requires Scope: none
	 *
	 * @return todo
	 */
	public List<Team> getTeams(Long channelId) {
		// Endpoint
		String requestUrl = String.format("/channels/%s/teams", channelId);

		// REST Request
		try {
			TeamList responseObject = restTemplate.getForObject(requestUrl, TeamList.class);

			return responseObject.getTeams();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return Collections.emptyList();
		}
	}

	/**
	 * Endpoint: Get Channel Subscribers
	 * Gets a list of users subscribed to a specified channel, sorted by the date when they subscribed.
	 * This is not related to the user messages, subscriptions are visible immediately.
	 * Requires Scope: channel_subscriptions
	 *
	 * @param limit     Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset    Object offset for pagination of results. Default: 0.
	 * @param order		Direction of sorting. Valid values: asc (oldest first), desc (newest first). Default: asc.
	 * @return todo
	 */
	public List<Subscription> getSubscriptions(ICredential credential, @Nullable Integer limit, @Nullable Integer offset, @Nullable Sort order) {
		try {
			checkScopePermission(credential.scopes(), Collections.singleton(Scope.CHANNEL_SUBSCRIPTIONS));
			// Endpoint
			String requestUrl = String.format("/channels/%s/subscriptions", credential.userId());
			RestTemplate restTemplate = this.restTemplate;

			// Query Parameters
			if (limit != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
			}
			if (offset != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
			}
			if (order != null) {
				restTemplate.getInterceptors().add(new QueryRequestInterceptor("direction", order.name().toLowerCase()));
			}

			// Header Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.accessToken())));

			SubscriptionList responseObject = restTemplate.getForObject(requestUrl, SubscriptionList.class);

			return responseObject.getSubscriptions();
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return Collections.emptyList();
		}
	}

	/**
	 * Endpoint: Check Channel Subscription by User
	 * Checks if a specified channel has a specified user subscribed to it. Intended for use by channel owners.
	 * Returns a subscription object which includes the user if that user is subscribed. Requires authentication for the channel.
	 * Requires Scope: channel_check_subscription
	 *
	 * @param user todo
	 * @return todo
	 */
	public Optional<Subscription> getSubscriptionByUser(ICredential credential, @Nonnull User user) {
		// Validate Arguments
		Objects.requireNonNull(user, "Please provide a User!");

		try {
			checkScopePermission(credential.scopes(), Collections.singleton(Scope.CHANNEL_SUBSCRIPTIONS));
			// Endpoint
			String requestUrl = String.format("/channels/%s/subscriptions/%s", credential.userId(), user.getId());
			RestTemplate restTemplate = this.restTemplate;

			Subscription responseObject = restTemplate.getForObject(requestUrl, Subscription.class);
			if (responseObject.getId() != null) {
				return Optional.of(responseObject);
			} else return Optional.empty();
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return Optional.empty();
		}
	}

	/**
	 * Endpoint: Get Channel Videos
	 * Gets a list of videos from a specified channel.
	 * Requires Scope: none
	 *
	 * @param limit          Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset         Object offset for pagination of results. Default: 0.
	 * @param sort          Sorting order of the returned objects. Valid values: views, time. Default: time (most recent first).
	 * @param language       Constrains the language of the videos that are returned; for example, *en,es.* Default: all languages.
	 * @param broadcastType Constrains the type of videos returned. Valid values: (any combination of) archive, highlight, upload, Default: highlight.
	 * @return todo
	 */
	// TODO: broadcast_type - Enumeric
	// TODO: language - Locale
	public List<Video> getVideos(Long channelId, @Nullable Integer limit, @Nullable Integer offset, @Nullable VideoSort sort, @Nullable List<Locale> language, @Nullable BroadcastType broadcastType) {
		// Endpoint
		String requestUrl = String.format("/channels/%s/videos", channelId);
		RestTemplate restTemplate = this.restTemplate;

		// Parameters
		if (limit != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", Integer.toString((limit > 100) ? 100 : (limit < 1) ? 25 : limit)));
		}
		if (offset != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", Integer.toString((offset < 0) ? 0 : offset)));
		}
		if (sort != null) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("sort", sort.name().toLowerCase()));
		}
		if (language != null && language.size() > 0) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("language", language.stream().map(Locale::getLanguage).collect(Collectors.joining(","))));
		}
		if (broadcastType != null && !broadcastType.equals(BroadcastType.ALL)) {
			restTemplate.getInterceptors().add(new QueryRequestInterceptor("broadcast_type", broadcastType.name().toLowerCase()));
		}

		// REST Request
		try {
			VideoList responseObject = restTemplate.getForObject(requestUrl, VideoList.class);

			return responseObject.getVideos();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return null;
		}
	}

// TODO: moving to TMI
// NOTE: using `/commercial (time)` in the chat
	/**
	 * Endpoint: Start Channel Commercial
	 * Starts a commercial (advertisement) on a specified channel. This is valid only for channels that are Twitch partners.
	 * You cannot start a commercial more often than once every 8 minutes.
	 * The length of the commercial (in seconds) is specified in the request body, with a required length parameter.
	 * Valid values are 30, 60, 90, 120, 150, and 180.
	 * Requires Scope: channel_commercial
	 *
	 * @param length todo
	 * @return todo
	 */
	public Commercial startCommercial(ICredential credential, Integer length) {
		Assert.isTrue(validCommercialLengths.contains(length), "Please provide a valid length! Valid: " + validCommercialLengths.toString());

		try {
			checkScopePermission(credential.scopes(), Collections.singleton(Scope.CHANNEL_COMMERCIAL));

			String requestUrl = String.format("/channels/%s/commercial", credential.userId());
			RestTemplate restTemplate = this.restTemplate;

			// Header Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.accessToken())));

			return restTemplate.postForObject(requestUrl, Collections.singletonMap("length", length), Commercial.class);

		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return null;
		}
	}

	/**
	 * Endpoint: Reset Channel Stream Key [!Irreversible]
	 * Deletes the stream key for a specified channel. Once it is deleted, the stream key is automatically reset.
	 * A stream key (also known as authorization key) uniquely identifies a stream.
	 * Each broadcast uses an RTMP URL that includes the stream key. Stream keys are assigned by Twitch.
	 * You will need to update your stream key or you will be unable to stream again.
	 * Requires Scope: channel_stream
	 *
	 * @return todo
	 */
	public Boolean deleteStreamKey(ICredential credential) {
		try {
			checkScopePermission(credential.scopes(), Collections.singleton(Scope.CHANNEL_STREAM));

			String requestUrl = String.format("/channels/%s/stream_key", credential.userId());
			RestTemplate restTemplate = this.restTemplate;

			// Header Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.accessToken())));

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

	public List<Community> getChannelCommunities(Long channelId) {
		String requestUrl = String.format("/channels/%s/communities", channelId);

		try {
			return restTemplate.getForObject(requestUrl, Communities.class).getCommunities();
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return null;
		}
	}

	public Boolean addCommunity(ICredential credential, List<Community> communities) {
		try {
			checkScopePermission(credential.scopes(), Collections.singleton(Scope.CHANNEL_EDITOR));

			String requestUrl = String.format("/channels/%s/communities", credential.userId());
			RestTemplate restTemplate = this.restTemplate;

			// Header Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.accessToken())));

			restTemplate.postForObject(requestUrl, Collections.singletonMap("community_ids", communities.stream().map(Community::getId).collect(Collectors.toList())), Void.class);

			return true;
		} catch (ScopeMissingException ex) {
			throw new ChannelCredentialMissingException(credential.userId(), ex);
		} catch (Exception ex) {
			log.error("Request failed: " + ex.getMessage());
			log.trace(ExceptionUtils.getStackTrace(ex));
			return false;
		}
	}


	public Boolean purgeCommunities(ICredential credential) {
		try {
			checkScopePermission(credential.scopes(), Collections.singleton(Scope.CHANNEL_EDITOR));

			String requestUrl = String.format("/channels/%s/communities", credential.userId());
			RestTemplate restTemplate = this.restTemplate;

			// Header Parameters
			restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", credential.accessToken())));

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
