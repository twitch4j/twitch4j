package me.philippheuer.twitch4j.events.event.irc.roomstates;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.model.Channel;

/**
 * R9K mode event.
 *
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class SubscribersOnlyEvent extends ChannelStatesEvent{
	public SubscribersOnlyEvent(Channel channel, boolean active) {
		super(channel, active);
	}
}
