package de.philippheuer.twitch4j.endpoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.util.Assert;

import de.philippheuer.twitch4j.TwitchClient;
import de.philippheuer.twitch4j.model.*;

import lombok.*;

@Getter
@Setter
public class ChannelEndpoint extends AbstractTwitchEndpoint {
	
	/**
	 * Channel ID
	 */
	private Long channelId;
	
	/**
	 * Commercial Lengths
	 */
	private final List<Long> validCommercialLengths = new ArrayList<Long>(Arrays.asList(30L, 60L, 90L, 120L, 150L, 180L));
	
	/**
	 * Get User by UserId
	 */
	public ChannelEndpoint(TwitchClient api, Long channelId) {
		super(api);
		
		// Validate Arguments
		Assert.notNull(channelId, "Please provide a Channel ID!");
		
		// Process Arguments
		setChannelId(channelId);
		
		
		
		
	}
	
	/**
	 * Endpoint: Get Channel
	 *  Gets a specified channel object.
	 * Requires Scope: none
	 */
	public Optional<Channel> getChannel() {
		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s", getApi().getTwitchEndpoint(), getChannelId());
			Channel responseObject = getRestTemplate().getForObject(requestUrl, Channel.class);
			
			return Optional.ofNullable(responseObject);
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * Endpoint: Get Channel Editors
	 *  Gets a list of users who are editors for a specified channel.
	 * Requires Scope: channel_read
	 */
	public Optional<List<User>> getEditors() {
		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s/editors", getApi().getTwitchEndpoint(), getChannelId());
			UserList responseObject = getRestTemplate().getForObject(requestUrl, UserList.class);
			
			return Optional.ofNullable(responseObject.getUsers());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * Endpoint: Get Channel Followers
	 *  Gets a list of users who follow a specified channel, sorted by the date when they started following the channel (newest first, unless specified otherwise).
	 * Requires Scope: none
	 */
	public Optional<List<Follow>> getFollowers() {
		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s/follows", getApi().getTwitchEndpoint(), getChannelId());
			FollowList responseObject = getRestTemplate().getForObject(requestUrl, FollowList.class);
			
			return Optional.ofNullable(responseObject.getFollows());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * Endpoint: Get Channel Teams
	 *  Gets a list of teams to which a specified channel belongs.
	 * Requires Scope: none
	 */
	public Optional<List<Team>> getTeams() {
		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s/teams", getApi().getTwitchEndpoint(), getChannelId());
			TeamList responseObject = getRestTemplate().getForObject(requestUrl, TeamList.class);
			
			return Optional.ofNullable(responseObject.getTeams());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * Endpoint: Get Channel Subscribers
	 *  Gets a list of users subscribed to a specified channel, sorted by the date when they subscribed.
	 *  This is not related to the user messages, subscriptions are visible immediately.
	 * Requires Scope: channel_subscriptions
	 */
	public Optional<List<Subscription>> getSubscriptions() {
		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s/subscriptions", getApi().getTwitchEndpoint(), getChannelId());
			SubscriptionList responseObject = getRestTemplate().getForObject(requestUrl, SubscriptionList.class);
			
			return Optional.ofNullable(responseObject.getSubscriptions());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * Endpoint: Get Channel Videos
	 *  Gets a list of users subscribed to a specified channel, sorted by the date when they subscribed.
	 *  This is not related to the user messages, subscriptions are visible immediately.
	 * Requires Scope: none
	 */
	public Optional<List<Video>> getVideos() {
		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s/videos", getApi().getTwitchEndpoint(), getChannelId());
			VideoList responseObject = getRestTemplate().getForObject(requestUrl, VideoList.class);
			
			return Optional.ofNullable(responseObject.getVideos());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * Endpoint: Check Channel Subscription by User
	 *  Checks if a specified channel has a specified user subscribed to it. Intended for use by channel owners.
	 *  Returns a subscription object which includes the user if that user is subscribed. Requires authentication for the channel.
	 * Requires Scope: channel_check_subscription
	 */
	public Boolean getSubscriptionByUser(User user) {
		// Validate Arguments
		Assert.notNull(user, "Please provide a User!");
		
		// Get Channel
		Channel channel = getChannel().get();
		
		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s/subscriptions/%d", getApi().getTwitchEndpoint(), getChannelId(), user.getId());
			Subscription responseObject = getRestTemplate().getForObject(requestUrl, Subscription.class);
			
			getLogger().debug(String.format("Found Subscription for Channel %s [%s] for User %s [%s].", channel.getDisplayName(), channel.getId(), responseObject.getUser().getDisplayName(), responseObject.getUser().getId()));
			
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * Endpoint: Start Channel Commercial
	 *  Starts a commercial (advertisement) on a specified channel. This is valid only for channels that are Twitch partners.
	 *  You cannot start a commercial more often than once every 8 minutes.
	 *  The length of the commercial (in seconds) is specified in the request body, with a required length parameter.
	 *  Valid values are 30, 60, 90, 120, 150, and 180.
	 * Requires Scope: channel_commercial
	 */
	public Boolean startCommercial(Long length) {
		// Validate Arguments
		Assert.isTrue(getValidCommercialLengths().contains(length), "Please provide a valid length! List: ");
		
		// @TODO: Implementation
		// and check response for success
		
		return false;
	}
	
	/**
	 * Endpoint: Reset Channel Stream Key [!Irreversible]
	 *  Deletes the stream key for a specified channel. Once it is deleted, the stream key is automatically reset.
	 *  A stream key (also known as authorization key) uniquely identifies a stream.
	 *  Each broadcast uses an RTMP URL that includes the stream key. Stream keys are assigned by Twitch.
	 *  You will need to update your stream key or you will be unable to stream again.
	 * Requires Scope: channel_stream
	 */
	public Boolean deleteStreamKey() {
		// Get Channel
		Channel channel = getChannel().get();
		
		// REST Request
		try {
			String requestUrl = String.format("%s/channels/%s/stream_key", getApi().getTwitchEndpoint(), getChannelId());
			getRestTemplate().delete(requestUrl);
			
			getLogger().warn(String.format("Deleted Stream Key for Channel %s [%s]!", channel.getDisplayName(), channel.getId()));
			
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
