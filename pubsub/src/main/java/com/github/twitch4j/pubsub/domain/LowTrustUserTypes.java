package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum LowTrustUserTypes {
    @JsonEnumDefaultValue
    UNKNOWN_TYPE,
    MANUALLY_ADDED,
    DETECTED_BAN_EVADER,
    BANNED_IN_SHARED_CHANNEL
}
