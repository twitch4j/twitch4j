package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * Bits Leaderboard Entry
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitsLeaderboardEntry {

    /** ID of the user (viewer) in the leaderboard entry. */
    @NonNull
    private String userId;

    /** Leaderboard rank of the user. */
    private Integer rank;

    /** Leaderboard score (number of Bits) of the user. */
    private Integer score;

}
