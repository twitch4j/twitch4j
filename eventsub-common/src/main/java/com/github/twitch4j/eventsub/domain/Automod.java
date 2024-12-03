package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.enums.TwitchEnum;
import com.github.twitch4j.common.util.TwitchEnumDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class Automod {

    /**
     * The category of the message offense.
     */
    @JsonDeserialize(using = TwitchEnumDeserializer.class)
    private TwitchEnum<AutomodCategory> category;

    /**
     * The level of severity (1-4).
     */
    private int level;

    /**
     * The bounds of the text that caused the message to be caught.
     */
    private List<Boundary> boundaries;

}
