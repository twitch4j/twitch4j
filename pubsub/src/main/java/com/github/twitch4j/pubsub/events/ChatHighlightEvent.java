package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ChatHighlight;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Unofficial
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class ChatHighlightEvent extends TwitchEvent {
    private String channelId;
    private String userId;
    private String msgId;
    private Instant chatSentAt;
    private Instant highlightsSentAt;
    private List<ChatHighlight> highlights;
}
