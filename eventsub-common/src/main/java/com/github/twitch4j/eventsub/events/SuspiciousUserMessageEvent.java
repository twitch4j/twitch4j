package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.AugmentedMessage;
import com.github.twitch4j.eventsub.domain.EvasionEvaluation;
import com.github.twitch4j.eventsub.domain.SuspiciousStatus;
import com.github.twitch4j.eventsub.domain.SuspiciousType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SuspiciousUserMessageEvent extends EventSubUserChannelEvent {

    /**
     * The status set for the suspicious user.
     */
    @JsonProperty("low_trust_status")
    private SuspiciousStatus status;

    /**
     * Channel IDs where the suspicious user is also banned.
     */
    @Nullable
    private Collection<String> sharedBanChannelIds;

    /**
     * User types (if any) that apply to the suspicious user.
     */
    private EnumSet<SuspiciousType> types;

    /**
     * A ban evasion likelihood value (if any) that as been applied to the user automatically by Twitch.
     */
    private EvasionEvaluation banEvasionEvaluation;

    /**
     * The structured chat message.
     */
    private AugmentedMessage message;

}
