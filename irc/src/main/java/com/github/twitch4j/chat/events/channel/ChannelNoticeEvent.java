package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;

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
	public ChannelNoticeEvent(ChatChannel channel, String msgId, String message) {
		super(channel);
		this.msgId = msgId;
		this.message = message;
	}
}
