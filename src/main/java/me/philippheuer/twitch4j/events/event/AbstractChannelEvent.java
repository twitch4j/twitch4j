package me.philippheuer.twitch4j.events.event;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.events.event.irc.ChannelStateEvent;
import me.philippheuer.twitch4j.message.irc.ChannelCache;
import me.philippheuer.twitch4j.model.Channel;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This event is a base for events that originate from a channel.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@EqualsAndHashCode(callSuper = false)
public class AbstractChannelEvent extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	@Getter(AccessLevel.NONE)
	private final ChannelCache cache;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 */
	public AbstractChannelEvent(Channel channel) {
		super();
		this.channel = channel;
		this.cache = getClient().getMessageInterface().getTwitchChat().getChannelCache().get(channel.getName());
	}

	/**
	 * Method to send messages to the channel the event originates from.
	 *
	 * @param message  The plain text of the message.
	 */
	public void sendMessage(String message) {
		getClient().getMessageInterface().sendMessage(getChannel().getName(), message);
	}

	public void sendWhisper(String username, String message) {
		getClient().getMessageInterface().sendPrivateMessage(username, message);
	}

	public void sendActionMessage(String message) {
		sendMessage(String.format("/me %s", message));
	}

	public void timeoutUser(String username, long time, String reason) {
		sendMessage(String.format("/timeout %s %d%s", username, time, (reason != null) ? " " + reason : ""));
	}
	public void timeoutUser(String username, long time) {
		timeoutUser(username, time, null);
	}

	public void banUser(String username, String reason) {
		sendMessage(String.format("/ban %s%s", username, (reason != null) ? " " + reason : ""));
	}
	public void banUser(String username) {
		banUser(username, null);
	}

	public void unbanUser(String username) {
		sendMessage(String.format("/unban %s", username));
	}

	public void enableSlow(long seconds) {
		Map<ChannelStateEvent.ChannelState, Object> states = cache.getChannelState();
		Assert.isTrue(seconds <= 0L, "Time must be greater and positively than 0");
		if (seconds > 0L) {
			sendMessage(String.format("/slow %d", (seconds > 0L) ? seconds : 30L));
		}
	}
	public void disableSlow() {
		long seconds = (Long) cache.getChannelState().get(ChannelStateEvent.ChannelState.SLOW);
		Assert.isTrue(seconds == 0L, "Slow mode is already disabled");
		if (seconds > 0L) {
			sendMessage("/slowoff");
		}
	}

	public void enableR9K() {
		boolean enabled = (Boolean) cache.getChannelState().get(ChannelStateEvent.ChannelState.R9K);
		Assert.isTrue(enabled, "R9k mode is already enabled");
		if (!enabled) {
			sendMessage("/r9kbeta");
		}
	}
	public void disableR9K() {
		boolean enabled = (Boolean) cache.getChannelState().get(ChannelStateEvent.ChannelState.R9K);
		Assert.isTrue(!enabled, "R9k mode is already disabled");
		if (enabled) {
			sendMessage("/r9kbetaoff");
		}
	}

	public void enableSubscribers() {
		boolean enabled = (Boolean) cache.getChannelState().get(ChannelStateEvent.ChannelState.SUBSCRIBERS);
		Assert.isTrue(enabled, "Subscribers mode is already enabled");
		if (!enabled) {
			sendMessage("/subscribers");
		}
	}
	public void disableSubscribers() {
		boolean enabled = (Boolean) cache.getChannelState().get(ChannelStateEvent.ChannelState.SUBSCRIBERS);
		Assert.isTrue(!enabled, "Subscribers mode is already disabled");
		if (enabled) {
			sendMessage("/subscribersoff");
		}
	}

	public void enableEmoteOnly() {
		boolean enabled = (Boolean) cache.getChannelState().get(ChannelStateEvent.ChannelState.EMOTE);
		Assert.isTrue(enabled, "Emote only mode is already enabled");
		if (!enabled) {
			sendMessage("/emoteonly");
		}
	}
	public void disableEmoteOnly() {
		boolean enabled = (Boolean) cache.getChannelState().get(ChannelStateEvent.ChannelState.EMOTE);
		Assert.isTrue(!enabled, "Emote only mode is already disabled");
		if (enabled) {
			sendMessage("/emoteonlyoff");
		}
	}

	public void enableFollowers(long seconds) {
		Map<ChannelStateEvent.ChannelState, Object> states = cache.getChannelState();
		Assert.isTrue(seconds <= 0L, "Time must be greater and positively than 0");
		if (seconds > 0L) {
			sendMessage(String.format("/followers%s", (seconds > 0L) ? " " + TimeUnit.SECONDS.toMinutes(seconds) : ""));
		}
	}
	public void disableFollowers() {
		long seconds = (Long) cache.getChannelState().get(ChannelStateEvent.ChannelState.SLOW);
		Assert.isTrue(seconds == -1L, "Slow mode is already disabled");
		if (seconds > 0L) {
			sendMessage("/followersoff");
		}
	}

	public void startHost(String channel) {
		sendMessage(String.format("/host %s", channel));
	}
	public void stopHost() {
		sendMessage("/unhost");
	}

	public void startComercial(long time) {
		sendMessage(String.format("/commercial%s", (time > 0L) ? " " + time : "" ));
	}

	public void clearChat() {
		sendMessage("/clear");
	}
}
