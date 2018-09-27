package me.philippheuer.twitch4j.message.irc.listener;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.github.philippheuer.events4j.EventManager;
import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.event.channel.*;
import me.philippheuer.twitch4j.events.event.irc.ChannelJoinEvent;
import me.philippheuer.twitch4j.events.event.irc.ChannelLeaveEvent;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageActionEvent;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.ChannelModEvent;
import me.philippheuer.twitch4j.events.event.irc.ChannelNoticeEvent;
import me.philippheuer.twitch4j.events.event.irc.ChannelStateEvent;
import me.philippheuer.twitch4j.events.event.irc.ClearChatEvent;
import me.philippheuer.twitch4j.events.event.irc.IRCMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.PrivateMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.UserBanEvent;
import me.philippheuer.twitch4j.events.event.irc.UserTimeoutEvent;
import me.philippheuer.twitch4j.message.irc.ChannelCache;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.Subscription;
import me.philippheuer.twitch4j.model.User;

/**
 * IRC Event Listener
 *
 * Listens for any irc triggered events and created the corresponding events for the EventDispatcher.
 */
@Getter
public class IRCEventListener {

	/**
	 * Twitch Client
	 */
	private final TwitchClient twitchClient;

	/**
	 * Event Manager
	 */
	private final EventManager eventManager;

	/**
	 * Constructor
	 *
	 * @param twitchClient The Twitch Client instance.
	 */
	public IRCEventListener(TwitchClient twitchClient) {
		this.twitchClient = twitchClient;
		this.eventManager = twitchClient.getEventManager();

		// event consumers
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onChannelMessage(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onWhisper(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onChannelCheer(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onChannelSubscription(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onClearChat(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onChannnelClientJoinEvent(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onChannnelClientLeaveEvent(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onChannelModChange(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onNoticeEvent(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onHostOnEvent(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onHostOffEvent(event));
		getEventManager().onEvent(IRCMessageEvent.class).subscribe(event -> onChannelState(event));
	}

	/**
	 * Channel Message Event
	 * @param event IRCMessageEvent
	 */
	public void onChannelMessage(IRCMessageEvent event) {
		if(event.getCommandType().equals("PRIVMSG")) {
			if(!event.getTags().containsKey("bits") && event.getMessage().isPresent()) {
				// Load Info
				Channel channel = event.getChannel();
				User user = event.getUser();

				// Dispatch Event
				if(event.getMessage().get().startsWith("\u0001ACTION ")) {
					// Action
					event.getClient().getEventManager().dispatchEvent(new ChannelMessageActionEvent(channel, user, event.getMessage().get().substring(8), event.getClientPermissions()));
				} else {
					// Regular Message
					event.getClient().getEventManager().dispatchEvent(new ChannelMessageEvent(channel, user, event.getMessage().get(), event.getClientPermissions()));
				}
			}
		}
	}

	/**
	 * Whisper Event
	 * @param event IRCMessageEvent
	 */
	public void onWhisper(IRCMessageEvent event) {
		if(event.getCommandType().equals("WHISPER")) {
			// Load Info
			User user = event.getUser();

			// Dispatch Event
			event.getClient().getEventManager().dispatchEvent(new PrivateMessageEvent(user, event.getMessage().get(), event.getClientPermissions()));
		}
	}

	/**
	 * Channel Cheer (Bits) Event
	 * @param event IRCMessageEvent
	 */
	public void onChannelCheer(IRCMessageEvent event) {
		if(event.getCommandType().equals("PRIVMSG")) {
			if(event.getTags().containsKey("bits")) {
				// Load Info
				Channel channel = event.getChannel();
				User user = event.getUser();
				String message = event.getMessage().orElse("");
				Integer bits = Integer.parseInt(event.getTags().get("bits"));

				// Dispatch Event
				event.getClient().getEventManager().dispatchEvent(new CheerEvent(channel, user, message, bits));
			}
		}
	}

	/**
	 * Channel Subscription Event
	 * @param event IRCMessageEvent
	 */
	public void onChannelSubscription(IRCMessageEvent event) {
		if (event.getCommandType().equals("USERNOTICE") && event.getTags().containsKey("msg-id")) {
			// Sub
			if(event.getTags().get("msg-id").equalsIgnoreCase("sub") || event.getTags().get("msg-id").equalsIgnoreCase("resub")) {
				// Load Info
				Channel channel = event.getChannel();
				User user = event.getUser();
				String subPlan = event.getTagValue("msg-param-sub-plan").get();
				boolean isResub = event.getTags().get("msg-id").equalsIgnoreCase("resub");
				Integer subStreak = (event.getTags().containsKey("msg-param-months")) ? Integer.parseInt(event.getTags().get("msg-param-months")) : 1;

				// twitch sometimes returns 0 months for new subs
				if(subStreak == 0) {
					subStreak = 1;
				}

				// Build Subscription Entity
				Subscription entity = new Subscription();
				entity.setCreatedAt(Instant.now());
				entity.setUser(user);
				entity.setSubPlanByCode(subPlan);

				// Dispatch Event
				event.getClient().getEventManager().dispatchEvent(new SubscriptionEvent(channel, entity, event.getMessage(), subStreak, false, null));
			}
			// Receive Gifted Sub
			else if(event.getTags().get("msg-id").equalsIgnoreCase("subgift")) {
				// Load Info
				Channel channel = event.getChannel();
				User user = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTagValue("msg-param-recipient-id").get()));
				User giftedBy = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTagValue("user-id").get()));
				String subPlan = event.getTagValue("msg-param-sub-plan").get();
				boolean isResub = event.getTags().get("msg-id").equalsIgnoreCase("resub");
				Integer subStreak = (event.getTags().containsKey("msg-param-months")) ? Integer.parseInt(event.getTags().get("msg-param-months")) : 1;

				// twitch sometimes returns 0 months for new subs
				if(subStreak == 0) {
					subStreak = 1;
				}

				// Build Subscription Entity
				Subscription entity = new Subscription();
				entity.setCreatedAt(Instant.now());
				entity.setUser(user);
				entity.setSubPlanByCode(subPlan);

				// Dispatch Event
				event.getClient().getEventManager().dispatchEvent(new SubscriptionEvent(channel, entity, event.getMessage(), subStreak, true, giftedBy));
			}
			// Gift X Subs
			else if(event.getTags().get("msg-id").equalsIgnoreCase("submysterygift")) {
				// Load Info
				Channel channel = event.getChannel();
				User user = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTagValue("user-id").get()));
				String subPlan = event.getTagValue("msg-param-sub-plan").get();
				Integer subsGifted = (event.getTags().containsKey("msg-param-mass-gift-count")) ? Integer.parseInt(event.getTags().get("msg-param-mass-gift-count")) : 0;
				Integer subsGiftedTotal = (event.getTags().containsKey("msg-param-sender-count")) ? Integer.parseInt(event.getTags().get("msg-param-sender-count")) : 0;

				// Dispatch Event
				event.getClient().getEventManager().dispatchEvent(new GiftSubscriptionsEvent(channel, user, subPlan, subsGifted, subsGiftedTotal));
			}
		}
	}

	/**
	 * Channel clearing chat, timeouting or banning user Event
	 * @param event IRCMessageEvent
	 */
	public void onClearChat(IRCMessageEvent event) {
		if (event.getCommandType().equals("CLEARCHAT")) {
			Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelId());
			if (event.getTags().containsKey("target-user-id")) { // ban or timeout
				if (event.getTags().containsKey("ban-duration")) { // timeout
					// Load Info
					User user = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTags().get("target-user-id")));
					Integer duration = Integer.parseInt(event.getTagValue("ban-duration").get());
					String banReason = event.getTags().get("ban-reason") != null ? event.getTags().get("ban-reason").toString() : "";
					banReason = banReason.replaceAll("\\\\s", " ");
					UserTimeoutEvent timeoutEvent = new UserTimeoutEvent(channel, user, duration, banReason);

					// Check ChannelCache to prevent duplicate events
					ChannelCache cache = getTwitchClient().getMessageInterface().getTwitchChat().getChannelCache().getOrDefault(channel.getName(), null);
					if(cache != null && cache.isTimeoutCached(timeoutEvent)) return;

					// Dispatch Event
					event.getClient().getEventManager().dispatchEvent(timeoutEvent);
				} else { // ban
					// Load Info
					User user = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTagValue("target-user-id").get()));
					String banReason = event.getTagValue("ban-reason").orElse("");
					banReason = banReason.replaceAll("\\\\s", " ");
					UserBanEvent banEvent = new UserBanEvent(channel, user, banReason);

					// Check ChannelCache to prevent duplicate events
					ChannelCache cache = getTwitchClient().getMessageInterface().getTwitchChat().getChannelCache().getOrDefault(channel.getName(), null);
					if(cache != null && cache.isBanCached(banEvent)) return;

					// Dispatch Event
					event.getClient().getEventManager().dispatchEvent(banEvent);
				}
			} else { // Clear chat event
				event.getClient().getEventManager().dispatchEvent(new ClearChatEvent(channel));
			}
		}
	}

	/**
	 * User Joins Channel Event
	 * @param event IRCMessageEvent
	 */
	public void onChannnelClientJoinEvent(IRCMessageEvent event) {
		if(event.getCommandType().equals("JOIN") && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
			// Load Info
			Channel channel = event.getChannel();
			User user = event.getUser();

			// Dispatch Event
			if (channel != null && user != null) {
				event.getClient().getEventManager().dispatchEvent(new ChannelJoinEvent(channel, user));
			}
		}
	}

	/**
	 * User Leaves Channel Event
	 * @param event IRCMessageEvent
	 */
	public void onChannnelClientLeaveEvent(IRCMessageEvent event) {
		if(event.getCommandType().equals("PART") && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
			// Load Info
			Channel channel = event.getChannel();
			User user = event.getUser();

			// Dispatch Event
			if (channel != null && user != null) {
				event.getClient().getEventManager().dispatchEvent(new ChannelLeaveEvent(channel, user));
			}
		}
	}

	/**
	 * Mod Status Change Event
	 * @param event IRCMessageEvent
	 */
	public void onChannelModChange(IRCMessageEvent event) {
		if(event.getCommandType().equals("MODE") && event.getPayload().isPresent()) {
			// Recieving Mod Status
			if(event.getPayload().get().substring(1).startsWith("o")) {
				// Load Info
				Channel channel = event.getChannel();
				User user = new User();
				user.setId(null);
				user.setName(event.getPayload().get().substring(3));
				user.setDisplayName(event.getPayload().get().substring(3));

				// Dispatch Event
				event.getClient().getEventManager().dispatchEvent(new ChannelModEvent(channel, user, event.getPayload().get().startsWith("+")));
			}
		}
	}

	public void onNoticeEvent(IRCMessageEvent event) {
		if (event.getCommandType().equals("NOTICE")) {
			Channel channel = event.getChannel();
			String messageId = event.getTagValue("msg-id").get();
			String message = event.getMessage().get();

			event.getClient().getEventManager().dispatchEvent(new ChannelNoticeEvent(channel, messageId, message));
		}
	}

	public void onHostOnEvent(IRCMessageEvent event) {
		if (event.getCommandType().equals("NOTICE")) {
			Channel channel = event.getChannel();
			String messageId = event.getTagValue("msg-id").get();

			if(messageId.equals("host_on")) {
				String message = event.getMessage().get();
				String targetChannelName = message.substring(12, message.length() - 1);
				Channel targetChannel = event.getClient().getChannelEndpoint().getChannel(targetChannelName);

				event.getClient().getEventManager().dispatchEvent(new HostOnEvent(channel, targetChannel));
			}
		}
	}

	public void onHostOffEvent(IRCMessageEvent event) {
		if (event.getCommandType().equals("NOTICE")) {
			Channel channel = event.getChannel();
			String messageId = event.getTagValue("msg-id").get();

			if(messageId.equals("host_off")) {
				event.getClient().getEventManager().dispatchEvent(new HostOffEvent(channel));
			}
		}
	}

	public void onChannelState(IRCMessageEvent event) {
		if(event.getCommandType().equals("ROOMSTATE")) {
			// getting Status on channel
			Channel channel = event.getChannel();
			Map<ChannelStateEvent.ChannelState, Object> states = new HashMap<ChannelStateEvent.ChannelState, Object>();
			if (event.getTags().size() > 2) {
				event.getTags().forEach((k, v) -> {
					switch (k) {
						case "broadcaster-lang":
							states.put(ChannelStateEvent.ChannelState.BROADCAST_LANG, (v != null) ? Locale.forLanguageTag(v) : v);
							break;
						case "emote-only":
							states.put(ChannelStateEvent.ChannelState.EMOTE, v.equals("1"));
							break;
						case "followers-only":
							states.put(ChannelStateEvent.ChannelState.FOLLOWERS, Long.parseLong(v));
							break;
						case "r9k":
							states.put(ChannelStateEvent.ChannelState.EMOTE, v.equals("1"));
							break;
						case "slow":
							states.put(ChannelStateEvent.ChannelState.SLOW, Long.parseLong(v));
							break;
						case "subs-only":
							states.put(ChannelStateEvent.ChannelState.EMOTE, v.equals("1"));
							break;
						default:
							break;
					}
				});
			}
			event.getClient().getEventManager().dispatchEvent(new ChannelStateEvent(channel, states));
		}
	}
}
