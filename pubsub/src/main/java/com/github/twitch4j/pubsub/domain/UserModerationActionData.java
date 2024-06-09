package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
@SuppressWarnings("unused")
public class UserModerationActionData {

    private String action;
    private String targetId;
    private String channelId;
    private @Nullable String reason;

    public boolean isBan() {
        return "ban".equals(action);
    }

    public boolean isUnban() {
        return "unban".equals(action);
    }

    public boolean isWarning() {
        return "warn".equals(action);
    }

    public boolean isWarningAcknowledgement() {
        return "acknowledge_warning".equals(action);
    }

}
