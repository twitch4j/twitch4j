package com.github.twitch4j.pubsub.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.RaidData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.NONE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RaidGoEvent extends TwitchEvent {
    private RaidData raid;
}
