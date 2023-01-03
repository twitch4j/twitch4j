package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class ShieldModeSettings {
    private String modeType; // "DEFENSE"
    private Instant lastModifiedAt;
    private SimpleUser lastModifiedBy;
    private Boolean clearChat;
    private AutomodLevelsModified.AutomodLevels automodSettings;
    private AccountVerificationOptions accountVerificationOptions;
    private Boolean subscribersOnly;
    private Boolean emoteOnly;
    private Boolean restrictFirstTimeChatters;
    private Boolean followersOnly;
    private Integer followersOnlyDuration;
    private Boolean slowMode;
    private Integer slowModeDuration;
    private Boolean chatDelay;
    private Integer chatDelayDuration;
    private List<Object> settingsErrors;
}
