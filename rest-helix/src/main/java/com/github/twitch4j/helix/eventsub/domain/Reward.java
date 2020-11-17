package com.github.twitch4j.helix.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reward {

    /**
     * The reward identifier.
     */
    private String id;

    /**
     * The reward name.
     */
    private String title;

    /**
     * The reward cost.
     */
    private Integer cost;

    /**
     * The reward description.
     */
    private String prompt;

}
