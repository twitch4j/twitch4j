package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.Automod;
import com.github.twitch4j.eventsub.domain.AutomodCaughtReason;
import com.github.twitch4j.eventsub.domain.BlockedTermWrapper;
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
public abstract class AutomodMessageEvent extends EventSubUserChannelEvent {

    /**
     * The ID of the message that was flagged by automod.
     */
    private String messageId;

    /**
     * The body of the message.
     */
    private Message message;

    /**
     * Whether the message was caught by automod or blocked term usage.
     */
    private AutomodCaughtReason reason;

    /**
     * Optional: If the message was caught by automod, this will be populated.
     */
    @Nullable
    private Automod automod;

    /**
     * Optional: If the message was caught due to a blocked term, this will be populated.
     */
    @Nullable
    private BlockedTermWrapper blockedTerm;

    /**
     * The timestamp of when automod saved the message.
     */
    private Instant heldAt;

}
