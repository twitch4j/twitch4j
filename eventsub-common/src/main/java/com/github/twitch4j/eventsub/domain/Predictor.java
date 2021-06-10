package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Predictor {

    /**
     * The ID of the user.
     */
    @JsonAlias("id")
    private String userId;

    /**
     * The login name of the user.
     */
    @JsonAlias("login")
    private String userLogin;

    /**
     * The display name of the user.
     */
    @JsonAlias("name")
    private String userName;

    /**
     * The number of Channel Points won.
     * <p>
     * This value is always null in the event payload for Prediction progress and Prediction lock.
     * This value is 0 if the outcome did not win or if the Prediction was canceled and Channel Points were refunded.
     */
    @Nullable
    private Integer channelPointsWon;

    /**
     * The number of Channel Points used to participate in the Prediction.
     */
    private Integer channelPointsUsed;

}
