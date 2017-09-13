package me.philippheuer.twitch4j.message.irc;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.irc.ChannelModEvent;
import me.philippheuer.twitch4j.model.User;

import java.util.ArrayList;
import java.util.List;

@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PUBLIC)
public class ChannelCache {
	/**
	 * Twitch Chat
	 */
	private final TwitchChat chat;

	/**
	 * The channel this cache instance is for
	 */
	private final String channel;

	/**
	 * Moderators
	 */
	private final List<User> moderators = new ArrayList<User>();

	/**
	 * Channel Cache
	 *
	 * @param chat TwitchChat Instance
	 * @param channel The channel this cache is for
	 */
	public ChannelCache(TwitchChat chat, String channel) {
		this.chat = chat;
		this.channel = channel;

		// Register for Events from the EventDispatcher
		chat.getTwitchClient().getDispatcher().registerListener(this);
	}

	/**
	 * Mod Grant/Removed
	 */
	@EventSubscriber
	public void onChannelModStatusChange(ChannelModEvent event) {
		if(event.getChannel().getName().equals(getChannel())) {
			// Add or remove moderator from cache
			if(event.isMod()) {
				if(!getModerators().contains(event.getUser())) {
					getModerators().add(event.getUser());
				}
			} else {
				getModerators().remove(event.getUser());
			}
		}
	}

}
