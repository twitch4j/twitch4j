package me.philippheuer.twitch4j.message.irc.listener;

import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.channel.CheerEvent;
import me.philippheuer.twitch4j.events.event.channel.SubscriptionEvent;
import me.philippheuer.twitch4j.events.event.irc.*;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.Subscription;
import me.philippheuer.twitch4j.model.User;

import java.util.Date;

/**
 * IRC Event Listener
 *
 * Listens for any irc triggered events and created the corresponding events for the EventDispatcher.
 */
public class IRCEventListener {

	/**
	 * Channel Message Event
	 * @param event IRCMessageEvent
	 */
	@EventSubscriber
	public void onChannelMessage(IRCMessageEvent event) {
		if(event.getCommandType().equals("PRIVMSG")) {
			if(!event.getTags().containsKey("bits") && event.getMessage().isPresent()) {
				// Load Info
				Channel channel = event.getClient().getChannelEndpoint(event.getChannelId()).getChannel();
				User user = event.getClient().getUserEndpoint().getUser(event.getUserId()).get();

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
			User user = event.getClient().getUserEndpoint().getUser(event.getUserId()).get();

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
				Channel channel = event.getClient().getChannelEndpoint(event.getChannelId()).getChannel();
				User user = event.getClient().getUserEndpoint().getUser(event.getUserId()).get();
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
				Channel channel = event.getClient().getChannelEndpoint(event.getChannel().get()).getChannel();
				User user = event.getClient().getUserEndpoint().getUserByUserName(event.getTagValue("login").get()).get();
				String subPlan = event.getTagValue("msg-param-sub-plan").get();
				boolean isResub = event.getTags().get("msg-id").toString().equalsIgnoreCase("resub");
				Integer subStreak = Integer.parseInt(event.getTags().get("msg-param-months"));

				// Twitch sometimes returns 0 months for new subs
				if(subStreak == 0) {
					subStreak = 1;
				}

				// Build Subscription Entity
				Subscription entity = new Subscription();
				entity.setCreatedAt(new Date());
				entity.setMessage(event.getMessage().orElse("")); // no message for sub time subs
				entity.setStreak(subStreak);
				entity.setUser(user);
				entity.setSubPlanByCode(subPlan);

				// Dispatch Event
				event.getClient().getDispatcher().dispatch(new SubscriptionEvent(channel, entity));
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
			Channel channel = event.getClient().getChannelEndpoint(event.getChannelId()).getChannel();
			if (event.getTags().containsKey("target-user-id")) { // ban or timeout
				if (event.getTags().containsKey("ban-duration")) { // timeout
					// Load Info
					User user = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTags().get("target-user-id"))).get();
					Integer duration = Integer.parseInt(event.getTagValue("ban-duration").get());
					String banReason = event.getTags().get("ban-reason") != null ? event.getTags().get("ban-reason").toString() : "";

					// Dispatch Event
					event.getClient().getDispatcher().dispatch(new UserTimeoutEvent(channel, user, duration, banReason));
				} else { // ban
					// Load Info
					User user = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTagValue("target-user-id").get())).get();
					String banReason = event.getTagValue("ban-reason").orElse("");

					// Dispatch Event
					event.getClient().getDispatcher().dispatch(new UserBanEvent(channel, user, banReason));
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
		if(event.getCommandType().equals("JOIN") && event.getChannel().isPresent() && event.getClientName().isPresent()) {
			// Load Info
			Channel channel = event.getClient().getChannelEndpoint(event.getChannel().get()).getChannel();
			User user = event.getClient().getUserEndpoint().getUserByUserName(event.getClientName().get()).get();

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
		if(event.getCommandType().equals("PART") && event.getChannel().isPresent() && event.getClientName().isPresent()) {
			// Load Info
			Channel channel = event.getClient().getChannelEndpoint(event.getChannel().get()).getChannel();
			User user = event.getClient().getUserEndpoint().getUserByUserName(event.getClientName().get()).get();

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
			if(event.getPayload().get().startsWith("+o")) {
				// Load Info
				Channel channel = event.getClient().getChannelEndpoint(event.getChannel().get()).getChannel();
				User user = event.getClient().getUserEndpoint().getUserByUserName(event.getPayload().get().substring(3)).get();

				// Dispatch Event
				event.getClient().getDispatcher().dispatch(new ChannelModEvent(channel, user, true));
			}
			// Losing Mod Status
			else if(event.getPayload().get().startsWith("-o")) {
				// Load Info
				Channel channel = event.getClient().getChannelEndpoint(event.getChannel().get()).getChannel();
				User user = event.getClient().getUserEndpoint().getUserByUserName(event.getPayload().get().substring(3)).get();

				// Dispatch Event
				event.getClient().getDispatcher().dispatch(new ChannelModEvent(channel, user, false));
			}
		}
	}
}
