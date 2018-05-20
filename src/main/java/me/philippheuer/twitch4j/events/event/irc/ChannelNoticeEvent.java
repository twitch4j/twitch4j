package me.philippheuer.twitch4j.events.event.irc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.event.ChannelBaseEvent;
import me.philippheuer.twitch4j.model.Channel;

@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelNoticeEvent extends ChannelBaseEvent {

	private final String msgId;
	private final String message;

	public ChannelNoticeEvent(Channel channel, String msgId, String message) {
		super(channel);
		this.msgId = msgId;
		this.message = message;
	}
}
