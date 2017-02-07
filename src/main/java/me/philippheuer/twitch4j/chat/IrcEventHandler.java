package me.philippheuer.twitch4j.chat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;
import org.kitteh.irc.client.library.element.MessageTag;
import org.kitteh.irc.client.library.element.ServerMessage;
import org.kitteh.irc.client.library.event.abstractbase.ClientReceiveServerMessageEventBase;
import org.kitteh.irc.client.library.event.abstractbase.ServerMessageEventBase;
import org.kitteh.irc.client.library.event.channel.*;
import org.kitteh.irc.client.library.event.client.ClientReceiveCommandEvent;
import org.kitteh.irc.client.library.event.helper.ClientReceiveServerMessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateNoticeEvent;
import org.kitteh.irc.client.library.event.user.ServerNoticeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.event.*;
import me.philippheuer.twitch4j.events.Event;
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
	private TwitchClient twitchClient;

	/**
	 * Holds the API Instance
	 */
	private IrcClient ircClient;

	/**
	 * Holds recent Subscriptions
	 */
	static private Map<String, Subscription> subscriptionHistory = ExpiringMap.builder()
			.expiration(5, TimeUnit.MINUTES)
			.expirationPolicy(ExpirationPolicy.CREATED)
			.build();

	/**
	 * Constructor
	 */
	public IrcEventHandler(TwitchClient twitchClient, IrcClient ircClient) {
		setTwitchClient(twitchClient);
		setIrcClient(ircClient);
	}

	/**
	 * Event: onClientReceiveCommand
	 *  Gets executed on NOTICE, USERNOTICE and simelar events.
	 */
	@Handler(priority=Integer.MAX_VALUE)
	public void onClientReceiveCommand(ClientReceiveServerMessageEventBase event) {

		/* For debugging
		if(!event.getCommand().equals("PRIVMSG")) {
			System.out.println(event.getCommand() + " | " + event.getServerMessage().toString());
		}
		*/

		/**
		 * About once every five minutes, you will receive a PING :tmi.twitch.tv from the server, in order to
		 * ensure that your connection to the server is not prematurely terminated, you should reply with PONG :tmi.twitch.tv.
		 */
		if(event.getOriginalMessage().contains("PING :tmi.twitch.tv")) {
			getIrcClient().getIrcClient().sendRawLine("PONG :tmi.twitch.tv");

			getLogger().debug("Responded to PING from Twitch IRC.");
		}

		// Handle Messages
		if(event.getCommand().equals("USERNOTICE") || event.getCommand().equals("PRIVMSG") || event.getCommand().equals("NOTICE") || event.getCommand().equals("CLEARCHAT") || event.getCommand().equals("HOSTTARGET ") || event.getCommand().equals("ROOMSTATE")) {
			// Get Channel on IRC
			String channelName = event.getParameters().get(0).replace("#", "");
			Long channelId = getTwitchClient().getUserEndpoint().getUserIdByUserName(channelName).get();
			Channel channel = getTwitchClient().getChannelEndpoint(channelId).getChannel();

			// Build Map from Tags
			Map<String, String> tagMap = new HashMap<>();
			for(MessageTag tag :  event.getServerMessage().getTags()) {
				if(tag.getValue().isPresent()) {
					tagMap.put(tag.getName(), tag.getValue().get());
				}
			}

			// Bans/Timeouts
			if(tagMap.containsKey("ban-reason")) {
				String banReason = tagMap.get("ban-reason");
				Long targetUserId = Long.parseLong(tagMap.get("target-user-id"));
				Optional<User> targetUser = getTwitchClient().getUserEndpoint().getUser(targetUserId);

				if(targetUser.isPresent()) {
					if(tagMap.containsKey("ban-duration")) {
						// Timeout
						Integer banDuration = Integer.parseInt(tagMap.get("ban-duration"));

						Event dispatchEvent = new UserTimeout(channel, targetUser.get(), banDuration, banReason);
						getTwitchClient().getDispatcher().dispatch(dispatchEvent);
					} else {
						// Permanent Ban
						Event dispatchEvent = new UserBan(channel, targetUser.get(), banReason);
						getTwitchClient().getDispatcher().dispatch(dispatchEvent);
					}
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
						Long userId = getTwitchClient().getUserEndpoint().getUserIdByUserName(matcher.group("userName")).get();
						// was: matcher.group("channel")
						onSubscription(userId, channel, 1, false, "");
						return;
					}

					// Subscription: Twitch Prime
					regExpr = Pattern.compile("^:twitchnotify!twitchnotify@twitchnotify\\.tmi\\.twitch\\.tv PRIVMSG #(?<channel>[a-zA-Z0-9_]{4,25}) :(?<userName>[a-zA-Z0-9_]{4,25}) just subscribed with Twitch Prime!$");
					matcher = regExpr.matcher(rawMessage);
					if(matcher.matches()) {
						Long userId = getTwitchClient().getUserEndpoint().getUserIdByUserName(matcher.group("userName")).get();
						// was: matcher.group("channel")
						onSubscription(userId, channel, 1, true, "");
						return;
					}
				}

				// Cheers
				if(tagMap.containsKey("bits")) {
					onCheer(Long.parseLong(tagMap.get("user-id")), channel, tagMap.get("bits"), event.getParameters().get(1));
				}
			}
			// Resubscriptions
			else if(event.getCommand().equals("USERNOTICE")) {
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
			// Notices
			else if(event.getOriginalMessage().toString().contains("NOTICE")) {
				ServerMessage message = event.getServerMessage();

				// Now hosting target_channel.
				if(tagMap.get("msg-id").equals("host_on")) {
					Event dispatchEvent = new HostOnEvent(channel, channel);
					getTwitchClient().getDispatcher().dispatch(dispatchEvent);
				}

				// Exited host mode.
				else if(tagMap.get("msg-id").equals("host_off")) {
					Event dispatchEvent = new HostOffEvent(channel);
					getTwitchClient().getDispatcher().dispatch(dispatchEvent);
				}

				// This room is now in emote-only mode.
				else if(tagMap.get("msg-id").equals("emote_only_on")) {
					// TODO: Trigger Event
				}

				// This room is no longer in emote-only mode.
				else if(tagMap.get("msg-id").equals("emote_only_off")) {
					// TODO: Trigger Event
				}

				// This channel has been suspended.
				else if(tagMap.get("msg-id").equals("msg_channel_suspended")) {
					// TODO: Trigger Event
				}

				// target_user has been timed out for ban_duration seconds.
				else if(tagMap.get("msg-id").equals("timeout_success")) {
					// TODO: Trigger Event
				}

				// target_user is now banned from this room.
				else if(tagMap.get("msg-id").equals("ban_success")) {
					// TODO: Trigger Event
				}

				// target_user is no longer banned from this room.
				else if(tagMap.get("msg-id").equals("unban_success")) {
					// TODO: Trigger Event
				}
			}
			// Roomstate
			else if(event.getCommand().equals("ROOMSTATE")) {
				if(tagMap.containsKey("subs-only")) {
					if(tagMap.get("subs-only").equals("0")) {
						// This room is no longer in subscribers-only mode.

					} else {
						// This room is now in subscribers-only mode.

					}
				}

				if(tagMap.containsKey("slow")) {
					if(tagMap.get("slow").equals("0")) {
						// This room is no longer in slow mode.
					} else {
						// This room is now in slow mode. You may send messages every slow_duration seconds.
						Integer messageDelay = Integer.parseInt(tagMap.get("slow"));

					}
				}

				if(tagMap.containsKey("r9k")) {
					if(tagMap.get("r9k").equals("0")) {
						// This room is no longer in r9k mode.
					} else {
						// This room is now in r9k mode.
					}
				}

				if(tagMap.containsKey("emote-only")) {
					if(tagMap.get("emote-only").equals("0")) {
						// This room is no longer in emote-only mode.
					} else {
						// This room is now in emote-only mode.
					}
				}

				if(tagMap.containsKey("followers-only")) {
					if(tagMap.get("followers-only").equals("0")) {
						// This room is no longer in followers-only mode.
					} else {
						// This room is now in followers-only mode.
					}
				}
			}
		}
	}

	/**
	 * Gets called when a new subscription is announced to the stream.
	 */
	private void onSubscription(Long userId, Channel channel, Integer streak, Boolean isPrime, String message) {
		// Build Subscription Entity
		Subscription entity = new Subscription();
		entity.setCreatedAt(Optional.ofNullable(new Date()));
		entity.setMessage(Optional.ofNullable(streak > 1 ? message : null)); // You can't write a message for the first sub.
		entity.setIsPrimeSub(Optional.ofNullable(isPrime));
		entity.setStreak(Optional.ofNullable(streak));
		entity.setUser(getTwitchClient().getUserEndpoint().getUser(userId).get());

		// Prevent multi-firing of the same subscription (is sometimes send 2. times)
		String subHistoryKey = String.format("%s|%s", entity.getUser().getId(), entity.getStreak());
		if(subscriptionHistory.containsKey(subHistoryKey)) {
			getLogger().trace(String.format("Subscription called two times, not firing event! %s", entity.toString()));
			return;
		} else {
			subscriptionHistory.put(subHistoryKey, entity);
		}

		// Debug
		getLogger().debug(String.format("%s subscribed to %s for the %s months. Prime=%s, Message=%s!", entity.getUser().getDisplayName(), channel.getName(), entity.getStreak(), entity.getIsPrimeSub(), entity.getMessage()));

		// Fire Event
		getTwitchClient().getDispatcher().dispatch(new SubscriptionEvent(channel, entity));
	}

	/**
	 * Gets called when a new cheer is announced to the stream.
	 */
	private void onCheer(Long userId, Channel channel, String bits, String message) {
		// Build Cheer Entity
		Cheer entity = new Cheer();
		entity.setBits(Integer.parseInt(bits));
		entity.setMessage(message);
		entity.setUser(getTwitchClient().getUserEndpoint().getUser(userId).get());

		// Debug
		getLogger().debug(String.format("%s just cheered to %s with %s bits. Message=%s!", entity.getUser().getDisplayName(), channel.getName(), bits.toString(), entity.getMessage()));

		// Fire Event
		getTwitchClient().getDispatcher().dispatch(new CheerEvent(channel, entity));
	}
}
