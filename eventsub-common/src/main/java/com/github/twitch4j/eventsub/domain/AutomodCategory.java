package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.github.twitch4j.common.annotation.Unofficial;

@Unofficial
public enum AutomodCategory {
    AGGRESSIVE,
    BULLYING,
    DISABILITY,
    SEXUALITY,
    MISOGYNY,
    RACISM,
    SEXUAL,
    PROFANITY,
    @JsonEnumDefaultValue
    UNKNOWN
}
