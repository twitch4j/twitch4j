package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * This event gets called when a channel gets a new follower.
 * <p>
 * Note: Despite the chat package, this event is <i>not</i> fired by IRC.
 * Instead, this event requires {@code TwitchClientHelper#enableFollowEventListener}
 * and the {@link com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_FOLLOWERS_READ} scope.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FollowEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private EventUser user;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 * @param user    The user who triggered the event.
	 */
	public FollowEvent(EventChannel channel, EventUser user) {
		super(channel);
		this.user = user;
	}
}
