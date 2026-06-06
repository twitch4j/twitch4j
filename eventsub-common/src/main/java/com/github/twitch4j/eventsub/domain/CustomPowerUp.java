package com.github.twitch4j.eventsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomPowerUp {

    /**
     * The title of the custom Power-up.
     */
    private String title;

    /**
     * The ID of the custom Power-up.
     */
    private String rewardId;

}
