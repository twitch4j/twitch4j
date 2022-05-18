package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelBanEvent extends EventSubModerationEvent {

    /**
     * The reason behind the ban.
     */
    private String reason;

    /**
     * The UTC date and time (in RFC3339 format) of when the user was banned or put in a timeout.
     */
    private Instant bannedAt;

    /**
     * Will be null if permanent ban. If it is a timeout, this field shows when the timeout will end.
     */
    @Nullable
    private Instant endsAt;

    /**
     * Indicates whether the ban is permanent (true) or a timeout (false). If true, {@link #getEndsAt()} will be null.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_permanent")
    private Boolean isPermanent;

}
