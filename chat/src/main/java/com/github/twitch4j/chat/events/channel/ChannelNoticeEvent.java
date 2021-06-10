package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.enums.NoticeTag;
import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * ChatChannel Notice Event
 */
@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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
     * The parsed value of {@link #getMsgId()}.
     */
    @Nullable
    NoticeTag type;

    /**
     * Channel Notice Event
     *
     * @param channel ChatChannel
     * @param msgId   Message Id
     * @param message message Content
     */
    public ChannelNoticeEvent(EventChannel channel, String msgId, String message) {
        super(channel);
        this.msgId = msgId;
        this.type = NoticeTag.parse(msgId);
        this.message = message;
    }

}
