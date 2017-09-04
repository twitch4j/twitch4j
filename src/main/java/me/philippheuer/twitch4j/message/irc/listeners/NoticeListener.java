package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.irc.ChannelModsEvent;
import me.philippheuer.twitch4j.events.event.irc.ChannelStateEvent;
import me.philippheuer.twitch4j.events.event.irc.IrcRawMessageEvent;
import me.philippheuer.twitch4j.model.Channel;

import java.util.Arrays;
import java.util.List;

/**
 * Twitch Notice Listener
 */
public class NoticeListener {

	/**
	 * Listens for the list of moderators
	 * @param event IRC Event
	 */
	@EventSubscriber
	public void onRoomState(IrcRawMessageEvent event) {
		if(event.getIrcParser().getTwitchCommandType().equals("ROOMSTATE")) {
			// Load User Info
			Channel channel = event.getClient().getChannelEndpoint(event.getIrcParser().getChannelName()).getChannel();
			Boolean subMode = Boolean.parseBoolean(event.getTag("subs-only"));
			Boolean emoteOnlyMode = Boolean.parseBoolean(event.getTag("emote-only"));
			Boolean r9kMode = Boolean.parseBoolean(event.getTag("r9k"));
			Integer slowMode = Integer.parseInt(event.getTag("slow"));
			Integer followersMode = Integer.parseInt(event.getTag("followers-only"));

			// Dispatch Event
			event.getClient().getDispatcher().dispatch(new ChannelStateEvent(channel, subMode, emoteOnlyMode, r9kMode, slowMode, followersMode));
		}
	}
}
