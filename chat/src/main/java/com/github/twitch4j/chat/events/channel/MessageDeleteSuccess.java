package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

/**
 * Successfully deleted a message
 *
 * @deprecated With chat commands decommissioned, this event is no longer fired; migrate to {@code TwitchHelix#deleteChatMessages}.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class MessageDeleteSuccess extends AbstractChannelEvent {

	/**
	 * Event Constructor
	 *
	 * @param channel       The channel that this event originates from.
	 */
	public MessageDeleteSuccess(EventChannel channel) {
		super(channel);
	}

}
