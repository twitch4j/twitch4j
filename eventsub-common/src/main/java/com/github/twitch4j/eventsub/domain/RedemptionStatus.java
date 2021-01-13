package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum RedemptionStatus {
    UNFULFILLED,
    FULFILLED,
    CANCELED,
    @JsonEnumDefaultValue
    UNKNOWN
}
