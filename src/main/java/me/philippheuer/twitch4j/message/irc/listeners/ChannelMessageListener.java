package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.ChannelMessageEvent;
import me.philippheuer.twitch4j.events.event.IrcRawMessageEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * Channel Message Listener
 *
 * Listens for normal message in a channel
 */
public class ChannelMessageListener {

	@EventSubscriber
	public void onRawIrcMessage(IrcRawMessageEvent event) {
		if(event.getIrcParser().getCommand().equals("PRIVMSG")) {
			if(!event.getIrcParser().getTags().hasTag("bits")) {
				// Load User Info
				Channel channel = event.getClient().getChannelEndpoint(event.getIrcParser().getChannelName()).getChannel();
				User user = event.getClient().getUserEndpoint().getUser(event.getIrcParser().getUserId()).get();

				// Dispatch Event
				ChannelMessageEvent channelMessageEvent = new ChannelMessageEvent(channel, user, event.getIrcParser().getMessage(), event.getIrcParser().getPermissions());
				event.getClient().getDispatcher().dispatch(channelMessageEvent);
			}
		}
	}

}
