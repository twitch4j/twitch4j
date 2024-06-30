package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum EvasionEvaluation {

    @JsonAlias("likely") // what docs claim
    LIKELY_EVADER,

    @JsonAlias("possible") // what docs claim
    POSSIBLE_EVADER,

    @JsonEnumDefaultValue
    UNKNOWN

}
