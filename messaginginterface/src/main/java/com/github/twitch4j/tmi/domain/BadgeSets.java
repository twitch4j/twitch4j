package com.github.twitch4j.tmi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@Data
@Setter(AccessLevel.PRIVATE)
public class BadgeSets {

    @JsonProperty("badge_sets")
    private Map<String, BadgeSet> badgesByName;

    public Optional<BadgeSet> getSubscriberBadgeSet() {
        return Optional.ofNullable(badgesByName.get("subscriber"));
    }

}
