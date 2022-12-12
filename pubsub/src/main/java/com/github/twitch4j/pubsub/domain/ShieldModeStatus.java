package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class ShieldModeStatus {
    @Accessors(fluent = true)
    @JsonProperty("is_shield_mode_active")
    private Boolean isShieldModeActive;
    private Instant shieldModeLastActivatedAt;
    private SimpleUser shieldModeLastActivatedBy;
    private Instant shieldModeLastDeactivatedAt;
    private SimpleUser shieldModeLastDeactivatedBy;
    private List<Object> settingsErrors;
}
