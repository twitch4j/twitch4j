package me.philippheuer.twitch4j.chat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kitteh.irc.client.library.element.MessageTag;
import org.kitteh.irc.client.library.element.ServerMessage;
import org.kitteh.irc.client.library.event.abstractbase.ClientReceiveServerMessageEventBase;
import org.kitteh.irc.client.library.event.abstractbase.ServerMessageEventBase;
import org.kitteh.irc.client.library.event.channel.*;
import org.kitteh.irc.client.library.event.client.ClientReceiveCommandEvent;
import org.kitteh.irc.client.library.event.helper.ClientReceiveServerMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.event.CheerEvent;
import me.philippheuer.twitch4j.events.event.SubscriptionEvent;
import me.philippheuer.twitch4j.model.Cheer;
import me.philippheuer.twitch4j.model.Subscription;
import net.engio.mbassy.listener.Handler;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

@Getter
@Setter
public class IrcEventHandler {
	
	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(IrcEventHandler.class);
	
	/**
	 * Holds the API Instance
	 */
	private TwitchClient client;
	
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
	public IrcEventHandler(TwitchClient client, IrcClient ircClient) {
		setClient(client);
		setIrcClient(ircClient);
	}
	
	/**
	 * Event: onClientReceiveCommand
	 *  Gets executed on NOTICE, USERNOTICE and simelar events.
	 */
	@Handler
	public void onClientReceiveCommand(ClientReceiveCommandEvent event) {
		if(event.getCommand().equals("USERNOTICE") || event.getCommand().equals("PRIVMSG")) {
			// Get Channel on IRC
			String channel = event.getParameters().get(0).replace("#", "");
			
			// Build Map from Tags
			Map<String, String> tagMap = new HashMap<>();
			for(MessageTag tag :  event.getServerMessage().getTags()) {
				if(tag.getValue().isPresent()) {
					tagMap.put(tag.getName(), tag.getValue().get());
				}
			}
			
			if(event.getCommand().equals("PRIVMSG")) {
				// First Subscribers Subscriptions
				String rawMessage = event.getServerMessage().getMessage();
				if(event.getServerMessage().getMessage().startsWith(":twitchnotify")) {
					Pattern regExpr = null;
					Matcher matcher = null;
					
					// Subscription: Normal
					regExpr = Pattern.compile("^:twitchnotify!twitchnotify@twitchnotify\\.tmi\\.twitch\\.tv PRIVMSG #(?<channel>[a-zA-Z0-9_]{4,25}) :(?<userName>[a-zA-Z0-9_]{4,25}) just subscribed!$");
					matcher = regExpr.matcher(rawMessage);
					if(matcher.matches()) {
						Long userId = getClient().getUserEndpoint().getUserIdByUserName(matcher.group("userName")).get();
						onSubscription(userId, matcher.group("channel"), 1, false, "");
						return;
					}
					
					// Subscription: Twitch Prime
					regExpr = Pattern.compile("^:twitchnotify!twitchnotify@twitchnotify\\.tmi\\.twitch\\.tv PRIVMSG #(?<channel>[a-zA-Z0-9_]{4,25}) :(?<userName>[a-zA-Z0-9_]{4,25}) just subscribed with Twitch Prime!$");
					matcher = regExpr.matcher(rawMessage);
					if(matcher.matches()) {
						Long userId = getClient().getUserEndpoint().getUserIdByUserName(matcher.group("userName")).get();
						onSubscription(userId, matcher.group("channel"), 1, true, "");
						return;
					}
				}
				
				// Cheers
				if(tagMap.containsKey("bits")) {
					onCheer(Long.parseLong(tagMap.get("user-id")), channel, tagMap.get("bits"), event.getParameters().get(1));
				}
			}
			
			// Resubscriptions
			if(event.getCommand().equals("USERNOTICE")) {
				// Get SubMessage if user wrote one
				Optional<String> subMessage = Optional.empty();
				if(event.getParameters().size() > 1) {
					subMessage = Optional.ofNullable(event.getParameters().get(1));
				}
				
				// Check Tags
				if(tagMap.containsKey("msg-id") && tagMap.containsKey("msg-param-months") && tagMap.containsKey("display-name") && tagMap.containsKey("system-msg")) {
					if(tagMap.get("msg-id").equals("resub") && Integer.parseInt(tagMap.get("msg-param-months")) > 1) {
						Boolean isPrime = tagMap.get("system-msg").toLowerCase().contains("twitch prime");
						
						onSubscription(Long.parseLong(tagMap.get("user-id")), channel, Integer.parseInt(tagMap.get("msg-param-months")), isPrime, subMessage.orElse(""));
						return;
					}
				}
			}
		}
	}
	
	/**
	 * Gets called when a new subscription is announced to the stream.
	 */
	private void onSubscription(Long userId, String channel, Integer streak, Boolean isPrime, String message) {
		// Build Subscription Entity
		Subscription entity = new Subscription();
		entity.setCreatedAt(Optional.ofNullable(new Date()));
		entity.setMessage(Optional.ofNullable(streak > 1 ? message : null)); // You can't write a message for the first sub.
		entity.setIsPrimeSub(Optional.ofNullable(isPrime));
		entity.setStreak(Optional.ofNullable(streak));
		entity.setUser(getClient().getUserEndpoint().getUser(userId).orElse(null));
		entity.setChannel(getClient().getChannelEndpoint(getClient().getUserEndpoint().getUserIdByUserName(channel).get()).getChannel().get());
		
		// Prevent multi-firing of the same subscription (is sometimes send 2. times)
		String subHistoryKey = String.format("%s|%s", entity.getUser().getId(), entity.getStreak());
		if(subscriptionHistory.containsKey(subHistoryKey)) {
			getLogger().trace(String.format("Subscription called two times, not firing event! %s", entity.toString()));
			return;
		} else {
			subscriptionHistory.put(subHistoryKey, entity);
		}
		
		// Debug
		getLogger().debug(String.format("%s subscribed to %s for the %s months. Prime=%s, Message=%s!", entity.getUser().getDisplayName(), channel, entity.getStreak(), entity.getIsPrimeSub(), entity.getMessage()));
		
		// Fire Event
		getClient().getDispatcher().dispatch(new SubscriptionEvent(entity));
	}
	
	/**
	 * Gets called when a new cheer is announced to the stream.
	 */
	private void onCheer(Long userId, String channel, String bits, String message) {
		// Build Cheer Entity
		Cheer entity = new Cheer();
		entity.setBits(Integer.parseInt(bits));
		entity.setMessage(message);
		entity.setUser(getClient().getUserEndpoint().getUser(userId).get());
		entity.setChannel(getClient().getChannelEndpoint(getClient().getUserEndpoint().getUserIdByUserName(channel).get()).getChannel().get());
		
		// Debug
		getLogger().debug(String.format("%s just cheered to %s with %s bits. Message=%s!", entity.getUser().getDisplayName(), channel, bits.toString(), entity.getMessage()));
		
		// Fire Event
		getClient().getDispatcher().dispatch(new CheerEvent(entity));
	}
}
