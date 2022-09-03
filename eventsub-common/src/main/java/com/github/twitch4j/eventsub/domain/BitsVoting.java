package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @deprecated Twitch no longer supports bits on polls.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Deprecated
public class BitsVoting {

    /**
     * Indicates if Bits can be used for voting.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_enabled")
    private Boolean isEnabled = false;

    /**
     * Number of Bits required to vote once with Bits.
     */
    private Integer amountPerVote = 0;

}
