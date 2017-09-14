package me.philippheuer.twitch4j.events.event.irc;

import lombok.*;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;

@Data
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class ClearChatEvent extends AbstractChannelEvent {

	public ClearChatEvent(Channel channel) {
		super(channel);
	}
}
