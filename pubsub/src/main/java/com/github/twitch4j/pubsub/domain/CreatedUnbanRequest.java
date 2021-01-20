package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreatedUnbanRequest extends UnbanRequest {

    private String channelId;

    private String requesterMessage;

    private String requesterProfileImage;

    private Instant createdAt;

}
