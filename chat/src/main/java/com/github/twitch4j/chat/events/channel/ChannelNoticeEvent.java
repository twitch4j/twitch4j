package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * ChatChannel Notice Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelNoticeEvent extends AbstractChannelEvent {

    /**
     * Message Id
     */
	private final String msgId;

    /**
     * Message Content
     */
	private final String message;

    /**
     * Channel Notice Event
     *
     * @param channel ChatChannel
     * @param msgId Message Id
     * @param message message Content
     */
	public ChannelNoticeEvent(EventChannel channel, String msgId, String message) {
		super(channel);
		this.msgId = msgId;
		this.message = message;
	}
}
