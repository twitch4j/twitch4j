package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

/**
 * This event gets called when a client gains/loses mod status.
 *
 * @deprecated <a href="https://discuss.dev.twitch.tv/t/irc-update-removing-mode-and-names-capabilities">Twitch intends to remove this event.</a>
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class ChannelModEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private EventUser user;

	/**
	 * Is Moderator?
	 */
	private boolean isMod;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 * @param user 		  The user that gained/lost mod status.
	 * @param isMod		  Did the use gain or lose mod status?
	 */
	public ChannelModEvent(EventChannel channel, EventUser user, boolean isMod) {
		super(channel);
		this.user = user;
		this.isMod = isMod;
	}
}
