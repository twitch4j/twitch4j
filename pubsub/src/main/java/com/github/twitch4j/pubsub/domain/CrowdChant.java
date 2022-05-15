package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

/**
 * @deprecated the crowd chant experiment was disabled by <a href="https://twitter.com/twitchsupport/status/1486036628523073539">Twitch</a> on 2022-02-02
 */
@Data
@Setter(AccessLevel.PRIVATE)
@Deprecated
public class CrowdChant {
    private String id;
    private String channelId;
    private ChannelPointsUser user;
    private String chatMessageId;
    private String text;
    private Instant createdAt;
    private Instant endsAt;
}
