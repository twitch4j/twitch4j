package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum SuspiciousStatus {
    @JsonAlias("none") // what docs falsely claim
    NO_TREATMENT,
    ACTIVE_MONITORING,
    RESTRICTED
}
