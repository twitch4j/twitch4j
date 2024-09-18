package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.SharedChatParticipant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelSharedChatBeginEvent extends SharedChatEvent {

    /**
     * The list of participants in the session.
     */
    private List<SharedChatParticipant> participants;

}
