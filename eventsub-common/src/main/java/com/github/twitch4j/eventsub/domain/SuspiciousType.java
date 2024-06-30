package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum SuspiciousType {

    @JsonAlias("manual") // what docs falsely claim
    MANUALLY_ADDED,

    @JsonAlias({"ban_evader_detector", "ban_evader"}) // what docs claim (yes, both)
    DETECTED_BAN_EVADER,

    @JsonAlias("shared_channel_ban") // what docs claim
    BANNED_IN_SHARED_CHANNEL,

    @JsonEnumDefaultValue
    UNKNOWN

}
