package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.ChannelMessageActionEvent;
import me.philippheuer.twitch4j.events.event.CheerEvent;
import me.philippheuer.twitch4j.events.event.IrcRawMessageEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

/**
 * Channel Message Listener
 *
 * Listens for normal message in a channel
 */
public class CheerListener {

	@EventSubscriber
	public void onRawIrcMessage(IrcRawMessageEvent event) {
		if(event.getIrcParser().getCommand().equals("PRIVMSG")) {
			if(event.getIrcParser().hasTag("bits")) {
				// Load User Info
				Channel channel = event.getClient().getChannelEndpoint(event.getIrcParser().getChannelName()).getChannel();
				User user = event.getClient().getUserEndpoint().getUser(event.getIrcParser().getUserId()).get();
				String message = event.getIrcParser().getMessage();
				Integer bits = Integer.parseInt(event.getIrcParser().getTag("bits"));

				// Dispatch Event
				event.getClient().getDispatcher().dispatch(new CheerEvent(channel, user, message, bits));
			}
		}
	}

}
