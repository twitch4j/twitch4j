package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.BitsVoting;
import com.github.twitch4j.eventsub.domain.ChannelPointsVoting;
import com.github.twitch4j.eventsub.domain.PollChoice;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ChannelPollEvent extends EventSubChannelEvent {

    /**
     * ID of the poll.
     */
    @JsonProperty("id")
    private String pollId;

    /**
     * Question displayed for the poll.
     */
    private String title;

    /**
     * Choices for the poll.
     */
    private List<PollChoice> choices;

    /**
     * The Bits voting settings for the poll.
     *
     * @deprecated Twitch no longer supports bits on polls.
     */
    @Deprecated
    private BitsVoting bitsVoting;

    /**
     * The Channel Points voting settings for the poll.
     */
    private ChannelPointsVoting channelPointsVoting;

    /**
     * The time the poll started.
     */
    private Instant startedAt;

}
