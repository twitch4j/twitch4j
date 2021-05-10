package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodContentClassification {

    private Category category;
    private Integer level;

    public enum Category {
        IDENTITY, SEXUAL, AGGRESSIVE, PROFANITY
    }

}
