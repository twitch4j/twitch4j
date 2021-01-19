package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class PresenceSettings {
    private Boolean shareActivity;
    private String availabilityOverride;
    private Boolean isInvisible;
    private String share;
}
