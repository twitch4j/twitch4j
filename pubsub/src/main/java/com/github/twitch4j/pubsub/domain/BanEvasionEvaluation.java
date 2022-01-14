package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum BanEvasionEvaluation {

    @JsonAlias({ "", "UNKNOWN_EVADER" })
    NOT_EVALUATED,

    UNLIKELY_EVADER,

    POSSIBLE_EVADER,

    LIKELY_EVADER

}
