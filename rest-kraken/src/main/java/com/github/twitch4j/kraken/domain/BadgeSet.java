package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
public class BadgeSet {
    private Map<String, Badge> versions;
}
