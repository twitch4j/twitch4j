package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.irc.IrcRawMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.PrivateMessageEvent;
import me.philippheuer.twitch4j.model.User;

/**
 * Channel Message Listener
 *
 * Listens for normal message in a channel
 */
public class WhisperListener {

	@EventSubscriber
	public void onRawIrcMessage(IrcRawMessageEvent event) {
		if(event.getIrcParser().getCommand().equals("WHISPER")) {
			// Load User Info
			User user = event.getClient().getUserEndpoint().getUser(event.getIrcParser().getUserId()).get();

			// Dispatch Event
			PrivateMessageEvent channelMessageEvent = new PrivateMessageEvent(user, event.getIrcParser().getMessage(), event.getIrcParser().getPermissions());
			event.getClient().getDispatcher().dispatch(channelMessageEvent);
		}
	}

}
