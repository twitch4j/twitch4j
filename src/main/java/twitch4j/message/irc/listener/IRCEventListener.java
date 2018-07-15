package twitch4j.message.irc.listener;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.Getter;
import twitch4j.TwitchClient;
import twitch4j.events.EventSubscriber;
import twitch4j.events.event.channel.CheerEvent;
import twitch4j.events.event.channel.HostOffEvent;
import twitch4j.events.event.channel.HostOnEvent;
import twitch4j.events.event.channel.SubscriptionEvent;
import twitch4j.events.event.irc.ChannelJoinEvent;
import twitch4j.events.event.irc.ChannelLeaveEvent;
import twitch4j.events.event.irc.ChannelMessageActionEvent;
import twitch4j.events.event.irc.ChannelMessageEvent;
import twitch4j.events.event.irc.ChannelModEvent;
import twitch4j.events.event.irc.ChannelNoticeEvent;
import twitch4j.events.event.irc.ChannelStateEvent;
import twitch4j.events.event.irc.ClearChatEvent;
import twitch4j.events.event.irc.IRCMessageEvent;
import twitch4j.events.event.irc.PrivateMessageEvent;
import twitch4j.events.event.irc.UserBanEvent;
import twitch4j.events.event.irc.UserTimeoutEvent;
import twitch4j.message.irc.ChannelCache;
import twitch4j.model.Channel;
import twitch4j.model.Subscription;
import twitch4j.model.User;

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
	 * Constructor
	 *
	 * @param twitchClient The Twitch Client instance.
	 */
	public IRCEventListener(TwitchClient twitchClient) {
		this.twitchClient = twitchClient;
	}

	/**
	 * Channel Message Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
	public void onChannelMessage(IRCMessageEvent event) {
		if(event.getCommandType().equals("PRIVMSG")) {
			if(!event.getTags().containsKey("bits") && event.getMessage().isPresent()) {
				// Load Info
				Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelId());
				User user = event.getClient().getUserEndpoint().getUser(event.getUserId());
				// Dispatch Event
				if(event.getMessage().get().startsWith("\u0001ACTION ")) {
					// Action
					event.getClient().getDispatcher().dispatch(new ChannelMessageActionEvent(channel, user, event.getMessage().get().substring(8), event.getClientPermissions()));
				} else {
					// Regular Message
					event.getClient().getDispatcher().dispatch(new ChannelMessageEvent(channel, user, event.getMessage().get(), event.getClientPermissions()));
				}
			}
		}
	}

	/**
	 * Whisper Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
	public void onWhisper(IRCMessageEvent event) {
		if(event.getCommandType().equals("WHISPER")) {
			// Load Info
			User user = event.getClient().getUserEndpoint().getUser(event.getUserId());

			// Dispatch Event
			event.getClient().getDispatcher().dispatch(new PrivateMessageEvent(user, event.getMessage().get(), event.getClientPermissions()));
		}
	}

	/**
	 * Channel Cheer (Bits) Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
	public void onChannelCheer(IRCMessageEvent event) {
		if(event.getCommandType().equals("PRIVMSG")) {
			if(event.getTags().containsKey("bits")) {
				// Load Info
				Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelId());
				User user = event.getClient().getUserEndpoint().getUser(event.getUserId());
				String message = event.getMessage().orElse("");
				Integer bits = Integer.parseInt(event.getTags().get("bits"));

				// Dispatch Event
				event.getClient().getDispatcher().dispatch(new CheerEvent(channel, user, message, bits));
			}
		}
	}

	/**
	 * Channel Subscription Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
	public void onChannelSubscription(IRCMessageEvent event) {
		if(event.getCommandType().equals("USERNOTICE") && event.getTags().containsKey("msg-id")) {
			if(event.getTags().get("msg-id").equalsIgnoreCase("sub") || event.getTags().get("msg-id").equalsIgnoreCase("resub")) {
				// Load Info
				Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelName().get());
				User user = event.getClient().getUserEndpoint().getUserByUserName(event.getTagValue("login").get());
				String subPlan = event.getTagValue("msg-param-sub-plan").get();
				boolean isResub = event.getTags().get("msg-id").equalsIgnoreCase("resub");
				Integer subStreak = (event.getTags().containsKey("msg-param-months")) ? Integer.parseInt(event.getTags().get("msg-param-months")) : 1;

				// Twitch sometimes returns 0 months for new subs
				if(subStreak == 0) {
					subStreak = 1;
				}

				// Build Subscription Entity
				Subscription entity = new Subscription();
				entity.setCreatedAt(Instant.now());
				entity.setUser(user);
				entity.setSubPlanByCode(subPlan);

				// Dispatch Event
				event.getClient().getDispatcher().dispatch(new SubscriptionEvent(channel, entity, event.getMessage(), subStreak));
			}
		}
	}

	/**
	 * Channel clearing chat, timeouting or banning user Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
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
					event.getClient().getDispatcher().dispatch(timeoutEvent);
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
					event.getClient().getDispatcher().dispatch(banEvent);
				}
			} else { // Clear chat event
				event.getClient().getDispatcher().dispatch(new ClearChatEvent(channel));
			}
		}
	}

	/**
	 * User Joins Channel Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
	public void onChannnelClientJoinEvent(IRCMessageEvent event) {
		if(event.getCommandType().equals("JOIN") && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
			// Load Info
			Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelName().get());
			User user = event.getClient().getUserEndpoint().getUserByUserName(event.getClientName().get());

			// Dispatch Event
			event.getClient().getDispatcher().dispatch(new ChannelJoinEvent(channel, user));
		}
	}

	/**
	 * User Leaves Channel Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
	public void onChannnelClientLeaveEvent(IRCMessageEvent event) {
		if(event.getCommandType().equals("PART") && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
			// Load Info
			Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelName().get());
			User user = event.getClient().getUserEndpoint().getUserByUserName(event.getClientName().get());

			// Dispatch Event
			event.getClient().getDispatcher().dispatch(new ChannelLeaveEvent(channel, user));
		}
	}

	/**
	 * Mod Status Change Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
	public void onChannelModChange(IRCMessageEvent event) {
		if(event.getCommandType().equals("MODE") && event.getPayload().isPresent()) {
			// Recieving Mod Status
			if(event.getPayload().get().substring(1).startsWith("o")) {
				// Load Info
				Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelName().get());
				User user = event.getClient().getUserEndpoint().getUserByUserName(event.getPayload().get().substring(3));

				// Dispatch Event
				event.getClient().getDispatcher().dispatch(new ChannelModEvent(channel, user, event.getPayload().get().startsWith("+")));
			}
		}
	}

	@EventSubscriber
	public void onNoticeEvent(IRCMessageEvent event) {
		if (event.getCommandType().equals("NOTICE")) {
			Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelName().get());
			String messageId = event.getTagValue("msg-id").get();
			String message = event.getMessage().get();

			event.getClient().getDispatcher().dispatch(new ChannelNoticeEvent(channel, messageId, message));
		}
	}

	@EventSubscriber
	public void onHostOnEvent(IRCMessageEvent event) {
		if (event.getCommandType().equals("NOTICE")) {
			Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelName().get());
			String messageId = event.getTagValue("msg-id").get();

			if(messageId.equals("host_on")) {
				String message = event.getMessage().get();
				String targetChannelName = message.substring(12, message.length() - 1);
				Channel targetChannel = event.getClient().getChannelEndpoint().getChannel(targetChannelName);

				event.getClient().getDispatcher().dispatch(new HostOnEvent(channel, targetChannel));
			}
		}
	}

	@EventSubscriber
	public void onHostOffEvent(IRCMessageEvent event) {
		if (event.getCommandType().equals("NOTICE")) {
			Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelName().get());
			String messageId = event.getTagValue("msg-id").get();

			if(messageId.equals("host_off")) {
				event.getClient().getDispatcher().dispatch(new HostOffEvent(channel));
			}
		}
	}

	@EventSubscriber
	public void onChannelState(IRCMessageEvent event) {
		if(event.getCommandType().equals("ROOMSTATE")) {
			// getting Status on channel
			Channel channel = event.getClient().getChannelEndpoint().getChannel(event.getChannelId());
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
			event.getClient().getDispatcher().dispatch(new ChannelStateEvent(channel, states));
		}
	}
}
