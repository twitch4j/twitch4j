package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.AutomaticReward;
import com.github.twitch4j.eventsub.domain.chat.Message;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AutomaticRewardRedemptionAddV2Event extends EventSubUserChannelEvent {

    /**
     * The ID of the Redemption.
     */
    @JsonProperty("id")
    private String redemptionId;

    /**
     * An object that contains the reward information.
     */
    private AutomaticReward reward;

    /**
     * An object that contains the user message and emote information needed to recreate the message.
     */
    @Nullable
    private Message message;

    /**
     * The UTC date and time (in RFC3339 format) of when the reward was redeemed.
     */
    private Instant redeemedAt;

}
