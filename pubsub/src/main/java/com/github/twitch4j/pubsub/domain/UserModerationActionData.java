package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class UserModerationActionData {

    private String action;
    private String targetId;
    private String channelId;

    public boolean isBan() {
        return "ban".equals(action);
    }

    public boolean isUnban() {
        return "unban".equals(action);
    }

}
