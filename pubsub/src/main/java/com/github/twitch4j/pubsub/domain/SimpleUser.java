package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class SimpleUser {

    @JsonAlias("id")
    private String userId;

    private String login;

    private String displayName;

}
