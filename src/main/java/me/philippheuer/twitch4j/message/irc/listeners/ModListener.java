package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.irc.IrcRawMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.UserBanEvent;
import me.philippheuer.twitch4j.events.event.irc.UserTimeoutEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * Mod Listener
 */
public class ModListener {

	/**
	 * Listens for Bans or Timeouts
	 * @param event IRC Event
	 */
	@EventSubscriber
	public void onBanOrTimeout(IrcRawMessageEvent event) {
		if(event.getIrcParser().getTwitchCommandType().equals("TIMEOUT")) // Timeout
		{
			// Load User Info
			Channel channel = event.getClient().getChannelEndpoint(event.getIrcParser().getChannelName()).getChannel();
			User user = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTag("target-user-id"))).get();
			Integer duration = Integer.parseInt(event.getTag("ban-duration"));
			String banReason = event.getTag("ban-reason") != null ? event.getTag("ban-reason").toString() : "";

			// Dispatch Event
			event.getClient().getDispatcher().dispatch(new UserTimeoutEvent(channel, user, duration, banReason));
		}
		else if(event.getIrcParser().getTwitchCommandType().equals("BAN")) // Permanent Ban
		{
			// Load User Info
			Channel channel = event.getClient().getChannelEndpoint(event.getIrcParser().getChannelName()).getChannel();
			User user = event.getClient().getUserEndpoint().getUser(Long.parseLong(event.getTag("target-user-id"))).get();
			String banReason = event.getTag("ban-reason") != null ? event.getTag("ban-reason").toString() : "";

			// Dispatch Event
			event.getClient().getDispatcher().dispatch(new UserBanEvent(channel, user, banReason));
		}
	}

}
