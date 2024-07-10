package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OutboundFollow {

    private String targetUserId;

    @JsonProperty("target_username")
    private String targetUserLogin;

    @JsonProperty("target_display_name")
    private String targetUserDisplayName;

    private Instant timestamp;

}
