package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Badge {
    private String id;
    private @JsonProperty("setID") String setId;
    private @JsonProperty("imageURL") String imageUrl;
}
