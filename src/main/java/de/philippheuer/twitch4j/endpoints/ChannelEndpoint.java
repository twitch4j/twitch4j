package de.philippheuer.twitch4j.endpoints;

import java.util.List;

import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.exception.*;
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
	 * Get User by UserId
	 */
	public ChannelEndpoint(Twitch4J api, Long channelId) {
		super(api);
		
		// Properties
		setChannelId(channelId);
	}
	
	
	/**
	 * Endpoint: Get Channel Followers
	 * Gets a list of users who follow a specified channel, sorted by the date when they started following the channel (newest first, unless specified otherwise).
	 * Requires Scope: None
	 */
	public List<Follow> getFollowers() {
		// REST Request
		String requestUrl = String.format("%s/channels/%s/follows", getApi().getTwitchEndpoint(), getChannelId());
		FollowList responseObject = getRestTemplate().getForObject(requestUrl, FollowList.class);
		List<Follow> entityList = responseObject.getFollows();
		
		return entityList;
	}
	
	/**
	 * Endpoint: Get Channel Teams
	 * Gets a list of teams to which a specified channel belongs.
	 * Requires Scope: None
	 */
	public List<Team> getTeams() {
		// REST Request
		String requestUrl = String.format("%s/channels/%s/teams", getApi().getTwitchEndpoint(), getChannelId());
		TeamList responseObject = getRestTemplate().getForObject(requestUrl, TeamList.class);
		List<Team> entityList = responseObject.getTeams();
		
		return entityList;
	}
	
	/**
	 * Endpoint: Get Channel Subscribers
	 * Gets a list of users subscribed to a specified channel, sorted by the date when they subscribed.
	 * This is not related to the user messages, subscriptions are visible immediately.
	 * Requires Scope: channel_subscriptions
	 */
	public List<Subscription> getSubscriptions() {
		// REST Request
		String requestUrl = String.format("%s/channels/%s/subscriptions", getApi().getTwitchEndpoint(), getChannelId());
		SubscriptionList responseObject = getRestTemplate().getForObject(requestUrl, SubscriptionList.class);
		List<Subscription> entityList = responseObject.getSubscriptions();
		
		return entityList;
	}
	
}
