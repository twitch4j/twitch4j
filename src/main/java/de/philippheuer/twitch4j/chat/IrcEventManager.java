package de.philippheuer.twitch4j.chat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kitteh.irc.client.library.element.MessageTag;
import org.kitteh.irc.client.library.element.ServerMessage;
import org.kitteh.irc.client.library.event.channel.*;
import org.kitteh.irc.client.library.event.client.ClientReceiveCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.philippheuer.twitch4j.TwitchClient;
import de.philippheuer.twitch4j.model.Subscription;
import lombok.*;
import net.engio.mbassy.listener.Handler;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

@Getter
@Setter
public class IrcEventManager {
	
	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(IrcEventManager.class);
	
	/**
	 * Holds the API Instance
	 */
	private TwitchClient api;
	
	/**
	 * Holds the API Instance
	 */
	private IrcClient ircClient;
	
	/**
	 * Holds recent Subscriptions
	 */
	static private Map<String, Subscription> subscriptionHistory = ExpiringMap.builder()
			.expiration(15, TimeUnit.MINUTES)
			.expirationPolicy(ExpirationPolicy.CREATED)
			.build();
	
	/**
	 * Constructor
	 */
	public IrcEventManager(TwitchClient api, IrcClient ircClient) {
		setApi(api);
		setIrcClient(ircClient);
	}
	
	/**
	 * Event: OnChannelNotice
	 *  Gets executed on channel notifications, on twitch those are subscriptions and follows.
	 */
	@Handler
	public void onChannelMessageA(ClientReceiveCommandEvent event) {
		// Filter to UserNotice (Resubs)
		if(event.getCommand().equals("USERNOTICE")) {
			// Get Usernotices from String
			String channel = event.getParameters().get(0).replace("#", "");
			
			// Get SubMessage if user wrote one
			Optional<String> subMessage = Optional.empty();
			if(event.getParameters().size() > 1) {
				subMessage = Optional.ofNullable(event.getParameters().get(1));
			}
			
			// Build HashMap
			Map<String, String> tagMap = new HashMap<>();
			for(MessageTag tag :  event.getServerMessage().getTags()) {
				if(tag.getValue().isPresent()) {
					tagMap.put(tag.getName(), tag.getValue().get());
				}
			}
			
			// Check Tags
			if(tagMap.containsKey("msg-id") && tagMap.containsKey("msg-param-months") && tagMap.containsKey("display-name") && tagMap.containsKey("system-msg")) {
				if(tagMap.get("msg-id").equals("resub") && Integer.parseInt(tagMap.get("msg-param-months")) > 1) {
					Boolean isPrime = tagMap.get("system-msg").toLowerCase().contains("twitch prime");
					
					onSubscription(tagMap.get("display-name"), channel, Integer.parseInt(tagMap.get("msg-param-months")), isPrime, subMessage.orElse(""));
					return;
				}
			}
		}
	}
	
	/**
	 * Event: OnChannelNotice
	 *  Gets executed on channel notifications, on twitch those are subscriptions and follows.
	 */
	@Handler
	public void onChannelMessage(ChannelMessageEvent event) {
		// Parse Message
		for(ServerMessage msg : event.getOriginalMessages()) {
			if(msg.getMessage().startsWith(":twitchnotify")) {
				String rawMessage = msg.getMessage();
				
				Pattern regExpr = null;
				Matcher matcher = null;
				
				// Subscription: Normal
				regExpr = Pattern.compile("^:twitchnotify!twitchnotify@twitchnotify\\.tmi\\.twitch\\.tv PRIVMSG #(?<channel>[a-zA-Z0-9_]{4,25}) :(?<userName>[a-zA-Z0-9_]{4,25}) just subscribed!$");
				matcher = regExpr.matcher(rawMessage);
				if(matcher.matches()) {
					onSubscription(matcher.group("userName"), matcher.group("channel"), 1, false, "");
					return;
				}
				
				// Subscription: Twitch Prime
				regExpr = Pattern.compile("^:twitchnotify!twitchnotify@twitchnotify\\.tmi\\.twitch\\.tv PRIVMSG #(?<channel>[a-zA-Z0-9_]{4,25}) :(?<userName>[a-zA-Z0-9_]{4,25}) just subscribed with Twitch Prime!$");
				matcher = regExpr.matcher(rawMessage);
				if(matcher.matches()) {
					onSubscription(matcher.group("userName"), matcher.group("channel"), 1, true, "");
					return;
				}
			}
		}
	}
	
	/**
	 * Gets called when a new subscription is announced to the stream.
	 */
	private void onSubscription(String userName, String channel, Integer streak, Boolean isPrime, String message) {
		// Build Subscription Entity
		Subscription sub = new Subscription();
		sub.setCreatedAt(new Date());
		sub.setMessage(streak > 1 ? message : ""); // You can't write a message for the first sub.
		sub.setIsPrimeSub(isPrime);
		sub.setStreak(streak);
		sub.setUser(getApi().getUserEndpoint().getUser(getApi().getUserEndpoint().getUserIdByUserName(userName).orElse(null)).orElse(null));
		
		// Prevent multi-firing of the same 
		String subHistoryKey = String.format("%s|%s", sub.getUser().getId(), sub.getStreak());
		if(subscriptionHistory.containsKey(subHistoryKey)) {
			getLogger().debug(String.format("Subscription called two times, not firing event! %s", sub.toString()));
			return;
		} else {
			subscriptionHistory.put(subHistoryKey, sub);
		}
		
		// Debug
		getLogger().debug(String.format("%s subscribed to %s for the %s months. Prime=%s, Message=%s!", sub.getUser().getDisplayName(), channel, sub.getStreak(), sub.getIsPrimeSub(), sub.getMessage()));
		
		// Fire Event
		// @TODO:
		
	}
}
