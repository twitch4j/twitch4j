package me.philippheuer.twitch4j.message.irc;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.irc.ChannelModEvent;
import me.philippheuer.twitch4j.events.event.irc.UserBanEvent;
import me.philippheuer.twitch4j.events.event.irc.UserTimeoutEvent;
import me.philippheuer.twitch4j.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
	private final List<User> moderators = new ArrayList<>();

	/**
	 * Timeouts
	 */
	private final List<UserTimeoutEvent> timeoutEvents = new CopyOnWriteArrayList<>();

	/**
	 * Bans
	 */
	private final List<UserBanEvent> banEvents = new CopyOnWriteArrayList<>();

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

	/**
	 * Timeout
	 */
	@EventSubscriber
	public void onChannelTimeout(UserTimeoutEvent event) {
		// Remove expired Events
		for (UserTimeoutEvent timeout : getTimeoutEvents()) {
			if (timeout.getCreatedAt().getTimeInMillis() + timeout.getDuration() * 1000 < Calendar.getInstance().getTimeInMillis()) {
				// Expired
				synchronized (timeoutEvents) {
					getTimeoutEvents().remove(timeout);
				}
			}
		}

		// Add to Cache
		if(!isTimeoutCached(event)) {
			synchronized (timeoutEvents) {
				getTimeoutEvents().add(event);
			}
		}
	}

	/**
	 * Ban
	 */
	@EventSubscriber
	public void onChannelBan(UserBanEvent event) {
		// Add to Cache
		if(!isBanCached(event)) {
			getBanEvents().add(event);
		}
	}

	/**
	 * Checks if the TimeoutEvent is in the ChannelCache
	 *
	 * @param event The UserTimeoutEvent
	 * @return Boolean
	 */
	public Boolean isTimeoutCached(UserTimeoutEvent event) {
		for(UserTimeoutEvent timeout : getTimeoutEvents()) {
			if(timeout.getUser().getId().equals(event.getUser().getId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the BanEvent is in the ChannelCache
	 *
	 * @param event The UserBanEvent
	 * @return Boolean
	 */
	public Boolean isBanCached(UserBanEvent event) {
		for(UserBanEvent ban : getBanEvents()) {
			if(ban.getUser().getId().equals(event.getUser().getId())) {
				return true;
			}
		}

		return false;
	}

}
