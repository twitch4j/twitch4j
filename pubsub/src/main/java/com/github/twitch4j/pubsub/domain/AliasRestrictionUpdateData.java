package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class AliasRestrictionUpdateData {

    @Accessors(fluent = true)
    @JsonProperty("user_is_restricted")
    private Boolean isRestricted;

    @JsonAlias("ChannelID")
    private String channelId;

}
