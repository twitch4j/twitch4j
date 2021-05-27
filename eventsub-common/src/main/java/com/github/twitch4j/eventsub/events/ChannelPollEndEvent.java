package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.PollStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelPollEndEvent extends ChannelPollEvent {

    /**
     * The status of the poll. Valid values are completed, archived, and terminated.
     */
    private PollStatus status;

    /**
     * The time the poll ended.
     */
    private Instant endedAt;

}
