package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class KrakenBlockTransaction {
    @JsonProperty("_id")
    private String id;

    private Instant updatedAt;

    @JsonProperty("user")
    private KrakenUser blockedUser;
}
