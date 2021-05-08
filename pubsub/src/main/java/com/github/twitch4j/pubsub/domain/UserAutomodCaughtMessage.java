package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class UserAutomodCaughtMessage {
    private String messageId;
    private AutomodCaughtMessageData.Status status;
}
