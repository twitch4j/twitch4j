package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.events.event.channel.DonationEvent;
import me.philippheuer.twitch4j.events.event.channel.FollowEvent;
import me.philippheuer.twitch4j.exceptions.ChannelCredentialMissingException;
import me.philippheuer.twitch4j.exceptions.ChannelDoesNotExistException;
import me.philippheuer.twitch4j.model.*;
import me.philippheuer.util.rest.HeaderRequestInterceptor;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

@Getter
@Setter
public class ChannelEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Commercial Lengths
	 */
	private final List<Long> validCommercialLengths = new ArrayList<Long>(Arrays.asList(30L, 60L, 90L, 120L, 150L, 180L));
	/**
	 * Channel ID
	 */
	private Long channelId;
	/**
	 * Event Timer
	 */
	private Timer eventTriggerTimer = new Timer(true);

	/**
	 * Event Timer - Checker: Last Follow
	 */
	private Date lastFollow;

	/**
	 * Event Timer - Checker: Last Donation
	 */
	private Calendar lastDonation;

	/**
	 * Constructor - by ChannelId
	 *
	 * @param client    todo
	 * @param channelId todo
	 */
	public ChannelEndpoint(TwitchClient client, Long channelId) {
		super(client);

		// Validate Arguments
		Assert.notNull(channelId, "Please provide a Channel ID!");

		// Process Arguments
		setChannelId(channelId);

		// Throw ChannelDoesNotExistException
		if (getChannel() == null) {
			throw new ChannelDoesNotExistException(channelId);
		}
	}

	/**
	 * Constructor - by ChannelName
	 *
	 * @param client      todo
	 * @param channelName todo
	 */
	public ChannelEndpoint(TwitchClient client, String channelName) {
		super(client);

		// Validate Arguments
		Assert.notNull(channelName, "Please provide a Channel Name!");

		// Process Arguments
		Optional<Long> userId = client.getUserEndpoint().getUserIdByUserName(channelName);
		if (userId.isPresent()) {
			setChannelId(userId.get());
		}

		// Throw ChannelDoesNotExistException
		if (getChannel() == null) {
			throw new ChannelDoesNotExistException(channelId);
		}
	}

	/**
	 * Endpoint: Get Channel
	 * Gets a specified channel object.
	 * Requires Scope: none
	 *
	 * @return todo
	 */
	public Channel getChannel() {
		// Endpoint
		String requestUrl = String.format("%s/channels/%s", Endpoints.API.getURL(), getChannelId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			if (!restObjectCache.containsKey(requestUrl)) {
				Channel responseObject = restTemplate.getForObject(requestUrl, Channel.class);
				restObjectCache.put(requestUrl, responseObject);
			}

			Channel responseObject = (Channel) restObjectCache.get(requestUrl);

			// Add twitch oauth credentials to channel object, if the credential manager has them
			{
				Optional<OAuthCredential> credential = getTwitchClient().getCredentialManager().getTwitchCredentialsForChannel(responseObject.getId());
				if (credential.isPresent()) {
					responseObject.setTwitchCredential(credential);
				}
			}

			// Add streamlabs oauth credentials to channel object, if the credential manager has them
			// Matching will be done using the channel id's, not credential aliases
			{
				for (Map.Entry<String, OAuthCredential> entryCredential : getTwitchClient().getCredentialManager().getOAuthCredentials().entrySet()) {
					if (entryCredential.getKey().startsWith("STREAMLABS-") && entryCredential.getValue().getUserId().equals(responseObject.getId())) {
						responseObject.setStreamlabsCredential(Optional.ofNullable(entryCredential.getValue()));
					}
				}
			}

			return responseObject;
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
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
	public Channel getChannelPrivilegied() {
		// Check Scope
		Optional<OAuthCredential> twitchCredential = getTwitchClient().getCredentialManager().getTwitchCredentialsForChannel(getChannelId());
		if (twitchCredential.isPresent()) {
			Set<String> requiredScopes = new HashSet<String>();
			requiredScopes.add(TwitchScopes.CHANNEL_READ.getKey());

			checkScopePermission(twitchCredential.get().getOAuthScopes(), requiredScopes);
		} else {
			throw new ChannelCredentialMissingException(getChannelId());
		}

		// Endpoint
		String requestUrl = String.format("%s/channel", Endpoints.API.getURL());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new HeaderRequestInterceptor("Authorization", String.format("OAuth %s", getChannel().getTwitchCredential().get().getToken())));

		// REST Request
		try {
			if (!restObjectCache.containsKey(requestUrl)) {
				Channel responseObject = getTwitchClient().getRestClient().getRestTemplate().getForObject(requestUrl, Channel.class);
				restObjectCache.put(requestUrl, responseObject);
			}

			Channel responseObject = (Channel) restObjectCache.get(requestUrl);

			return responseObject;
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
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
	public List<User> getEditors() {
		// Check Scope
		Optional<OAuthCredential> twitchCredential = getTwitchClient().getCredentialManager().getTwitchCredentialsForChannel(getChannelId());
		if (twitchCredential.isPresent()) {
			Set<String> requiredScopes = new HashSet<String>();
			requiredScopes.add(TwitchScopes.CHANNEL_READ.getKey());

			checkScopePermission(twitchCredential.get().getOAuthScopes(), requiredScopes);
		} else {
			throw new ChannelCredentialMissingException(getChannelId());
		}

		// Endpoint
		String requestUrl = String.format("%s/channels/%s/editors", Endpoints.API.getURL(), getChannelId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			UserList responseObject = restTemplate.getForObject(requestUrl, UserList.class);

			return responseObject.getUsers();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
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
	public FollowList getFollowers(Optional<Long> limit, Optional<String> cursor, Optional<String> direction) {
		// Endpoint
		String requestUrl = String.format("%s/channels/%s/follows", Endpoints.API.getURL(), getChannelId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("cursor", cursor.orElse("")));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("direction", direction.orElse("desc").toString()));

		// REST Request
		try {
			FollowList responseObject = restTemplate.getForObject(requestUrl, FollowList.class);

			// Provide the Follow with info about the channel
			for(Follow f : responseObject.getFollows()) {
				f.setChannel(getChannel());
			}

			return responseObject;
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
			return null;
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
	public List<Follow> getFollowers(Optional<Long> limit, Optional<String> direction) {
		if (limit.isPresent()) {
			if (limit.get() > 100) {
				List<Follow> resultList = new ArrayList<Follow>();

				Long recordsToFetch = limit.get();
				String cursor = "";

				while (recordsToFetch > 0) {
					FollowList followList = getFollowers(Optional.ofNullable(recordsToFetch > 100 ? 100 : recordsToFetch), Optional.empty(), Optional.empty());
					cursor = followList.getCursor();
					Integer results = followList.getFollows().size();
					resultList.addAll(followList.getFollows());
					recordsToFetch -= results;

					if (results == 0) {
						break;
					}
				}

				return resultList;
			} else {
				return getFollowers(limit, Optional.empty(), direction).getFollows();
			}
		} else {
			return getFollowers(Optional.empty(), Optional.empty(), direction).getFollows();
		}
	}

	/**
	 * Endpoint: Get Channel Teams
	 * Gets a list of teams to which a specified channel belongs.
	 * Requires Scope: none
	 *
	 * @return todo
	 */
	public List<Team> getTeams() {
		// Endpoint
		String requestUrl = String.format("%s/channels/%s/teams", Endpoints.API.getURL(), getChannelId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			TeamList responseObject = restTemplate.getForObject(requestUrl, TeamList.class);

			return responseObject.getTeams();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
			return null;
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
	 * @param direction Direction of sorting. Valid values: asc (oldest first), desc (newest first). Default: desc.
	 * @return todo
	 */
	public List<Subscription> getSubscriptions(Optional<Long> limit, Optional<Long> offset, Optional<String> direction) {
		// Check Scope
		if (getChannel().getTwitchCredential().isPresent()) {
			Set<String> requiredScopes = new HashSet<String>();
			requiredScopes.add(TwitchScopes.CHANNEL_SUBSCRIPTIONS.getKey());

			checkScopePermission(getChannel().getTwitchCredential().get().getOAuthScopes(), requiredScopes);
		} else {
			throw new ChannelCredentialMissingException(getChannelId());
		}

		// Endpoint
		String requestUrl = String.format("%s/channels/%s/subscriptions", Endpoints.API.getURL(), getChannelId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(getChannel().getTwitchCredential().get());

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("direction", direction.orElse("desc").toString()));

		// REST Request
		try {
			SubscriptionList responseObject = restTemplate.getForObject(requestUrl, SubscriptionList.class);

			return responseObject.getSubscriptions();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
			return null;
		}
	}

	/**
	 * Endpoint: Check Channel Subscription by User
	 * Checks if a specified channel has a specified user subscribed to it. Intended for use by channel owners.
	 * Returns a subscription object which includes the user if that user is subscribed. Requires authentication for the channel.
	 * Requires Scope: channel_check_subscription
	 * TODO: Handle error
	 *
	 * @param user todo
	 * @return todo
	 */
	public Boolean getSubscriptionByUser(User user) {
		// Check Scope
		if (getChannel().getTwitchCredential().isPresent()) {
			Set<String> requiredScopes = new HashSet<String>();
			requiredScopes.add(TwitchScopes.CHANNEL_CHECK_SUBSCRIPTION.getKey());

			checkScopePermission(getChannel().getTwitchCredential().get().getOAuthScopes(), requiredScopes);
		} else {
			throw new ChannelCredentialMissingException(getChannelId());
		}

		// Validate Arguments
		Assert.notNull(user, "Please provide a User!");

		// Endpoint
		String requestUrl = String.format("%s/channels/%s/subscriptions/%d", Endpoints.API.getURL(), getChannelId(), user.getId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(getChannel().getTwitchCredential().get());

		// REST Request
		try {
			Subscription responseObject = restTemplate.getForObject(requestUrl, Subscription.class);

			if (responseObject.getId() != null) {
				return true;
			}

		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
		}

		return false;
	}

	/**
	 * Endpoint: Get Channel Videos
	 * Gets a list of videos from a specified channel.
	 * Requires Scope: none
	 *
	 * @param limit          Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset         Object offset for pagination of results. Default: 0.
	 * @param sort           Sorting order of the returned objects. Valid values: views, time. Default: time (most recent first).
	 * @param language       Constrains the language of the videos that are returned; for example, *en,es.* Default: all languages.
	 * @param broadcast_type Constrains the type of videos returned. Valid values: (any combination of) archive, highlight, upload, Default: highlight.
	 * @return todo
	 */
	public List<Video> getVideos(Optional<Long> limit, Optional<Long> offset, Optional<String> sort, Optional<String> language, Optional<String> broadcast_type) {
		// Endpoint
		String requestUrl = String.format("%s/channels/%s/videos", Endpoints.API.getURL(), getChannelId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("sort", sort.orElse("time").toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("language", language.orElse(null).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("broadcast_type", broadcast_type.orElse("highlight").toString()));

		// REST Request
		try {
			VideoList responseObject = restTemplate.getForObject(requestUrl, VideoList.class);

			return responseObject.getVideos();
		} catch (Exception ex) {
			Logger.error(this, "Request failed: " + ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
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
	public Boolean startCommercial(Long length) {
		// Check Scope
		if (getChannel().getTwitchCredential().isPresent()) {
			Set<String> requiredScopes = new HashSet<String>();
			requiredScopes.add(TwitchScopes.CHANNEL_COMMERCIAL.getKey());

			checkScopePermission(getChannel().getTwitchCredential().get().getOAuthScopes(), requiredScopes);
		} else {
			throw new ChannelCredentialMissingException(getChannelId());
		}

		// Validate Arguments
		Assert.isTrue(getValidCommercialLengths().contains(length), "Please provide a valid length! Valid: " + getValidCommercialLengths().toString());

		// @TODO: Implementation
		// and check response for success

		return false;
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
	public Boolean deleteStreamKey() {
		// Check Scope
		if (getChannel().getTwitchCredential().isPresent()) {
			Set<String> requiredScopes = new HashSet<String>();
			requiredScopes.add(TwitchScopes.CHANNEL_STREAM.getKey());

			checkScopePermission(getChannel().getTwitchCredential().get().getOAuthScopes(), requiredScopes);
		} else {
			throw new ChannelCredentialMissingException(getChannelId());
		}

		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s/stream_key", Endpoints.API.getURL(), getChannelId());
			getTwitchClient().getRestClient().getPrivilegedRestTemplate(getChannel().getTwitchCredential().get()).delete(requestUrl);

			return true;
		} catch (Exception ex) {
			return false;
		}
	}
// TODO: moving to TMI
	/**
	 * IRC: Ban User
	 * This command will allow you to permanently ban a user from the chat room.
	 *
	 * @param user Username.
	 */
	public void ban(String user) {
		getTwitchClient().getMessageInterface().sendMessage(getChannel().getName(), String.format(".ban %s", user));
	}
// TODO: moving to TMI
	/**
	 * IRC: Unban User
	 * This command will allow you to lift a permanent ban on a user from the chat room. You can also use this command to end a ban early; this also applies to timeouts.
	 *
	 * @param user Username.
	 */
	public void unban(String user) {
		getTwitchClient().getMessageInterface().sendMessage(getChannel().getName(), String.format(".unban %s", user));
	}
// TODO: moving to TMI
	/**
	 * IRC: Timeout User
	 * This command allows you to temporarily ban someone from the chat room for 10 minutes by default.
	 * This will be indicated to yourself and the temporarily banned subject in chat on a successful temporary ban.
	 * A new timeout command will overwrite an old one.
	 *
	 * @param user Username.
	 * @param duration {@link Duration} in seconds
	 */
	public void timeout(String user, Duration duration) {
		getTwitchClient().getMessageInterface().sendMessage(getChannel().getName(), String.format(".timeout %s %s", user, duration.getSeconds()));
	}
	// TODO: moving to TMI

	/**
	 * IRC: Purge Chat of User
	 * Clears all messages in a channel.
	 *
	 * @param user          User.
	 */
	public void purgeChat(String user) {
		timeout(user, Duration.ofSeconds(1));
	}

	// TODO: moving to TMI
	/**
	 * IRC: Purge Chat
	 * This command will allow the Broadcaster and chat moderators to completely wipe the previous chat history.
	 */
	public void purgeChat() {
		getTwitchClient().getMessageInterface().sendMessage(getChannel().getName(), ".clear");
	}

	/**
	 * Central Endpoint: Register Channel Event Listener
	 * IRC: Subscriptions, Bits
	 * Rest API: Follows
	 * Streamlabs API: Donations
	 */
	public void registerEventListener() {
		// Check that the channel exists
		// TODO

		// Check Endpoint Status
		// - Check Rest API
		// - Check IRC
		{
			Map.Entry<Boolean, String> result = getTwitchClient().getMessageInterface().getTwitchChat().checkEndpointStatus();
			if (!result.getKey()) {
				Logger.warn(this, "IRC Client not operating. You will not receive any irc events! [" + result.getValue() + "]");
			}
		}
		// - Check PubSub
		if (!getTwitchClient().getMessageInterface().getPubSub().checkEndpointStatus()) {
			// We can ignore this right now, because we will reconnect as soon as pubsub is back up.
			Logger.warn(this, "PubSub Client not operating. You will not recieve any pubsub events!");
		}

		// Get Channel Information
		Channel channel = getChannel();
		// - Listen: IRC
		getTwitchClient().getMessageInterface().joinChannel(channel.getName());
		// - Listen: PubSub
		// NYI

		// Event Timer
		// - Follows
		startFollowListener(channel);
	}

	private void startFollowListener(Channel channel) {
		// Define Action
		TimerTask action = new TimerTask() {
			public void run() {
				try {
					// Followers
					List<Date> creationDates = new ArrayList<Date>();
					List<Follow> followList = getFollowers(
							Optional.ofNullable(10l),
							Optional.empty(),
							Optional.empty()
					).getFollows();
					if (followList.size() > 0) {
						for (Follow follow : followList) {
							// dispatch event for new follows only
							if (lastFollow != null && follow.getCreatedAt().after(lastFollow)) {
								Event dispatchEvent = new FollowEvent(channel, follow.getUser());
								getTwitchClient().getDispatcher().dispatch(dispatchEvent);
							}
							creationDates.add(follow.getCreatedAt());
						}

						// Get newest date from all follows
						Date lastFollowNew = creationDates.stream().max(Date::compareTo).get();
						if (lastFollow == null || lastFollowNew.after(lastFollow)) {
							lastFollow = lastFollowNew;
						}
					}
				} catch (Exception ex) {
					Logger.warn(this, "Couldn't fetch Followers to trigger FollowEvents!");

					// Delay next execution
					try {
						Thread.sleep(1000);
					} catch (Exception et) {
						Logger.error(this, ExceptionUtils.getStackTrace(et));
					}
				}
			}
		};

		// Schedule Action
		eventTriggerTimer.scheduleAtFixedRate(action, 0, 5 * 1000);
	}

	/**
	 * Cancel Timer/Listeners
	 */
	public void cancel() {
		eventTriggerTimer.cancel();
	}
}
