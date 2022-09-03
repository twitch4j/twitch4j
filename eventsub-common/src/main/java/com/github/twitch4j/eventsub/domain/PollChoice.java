package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PollChoice {

    /**
     * ID for the choice.
     */
    private String id;

    /**
     * Text displayed for the choice. Maximum: 25 characters.
     */
    private String title;

    /**
     * Total number of votes received for the choice across all methods of voting.
     */
    @Nullable
    private Integer votes;

    /**
     * Number of votes received via Channel Points.
     */
    @Nullable
    private Integer channelPointsVotes;

    /**
     * Number of votes received via Bits.
     *
     * @deprecated Twitch no longer supports bits on polls.
     */
    @Nullable
    @Deprecated
    private Integer bitsVotes;

}
