package com.github.twitch4j.kraken.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenFollow {

    @JsonProperty("created_at")
    private Instant followedAt;

    private Boolean notifications;

    private KrakenUser user;

}
