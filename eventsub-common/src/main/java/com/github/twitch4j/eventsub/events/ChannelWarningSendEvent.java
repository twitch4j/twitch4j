package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelWarningSendEvent extends EventSubModerationEvent {

    /**
     * Optional: The reason given for the warning.
     */
    @Nullable
    private String reason;

    /**
     * Optional: The chat rules cited for the warning.
     */
    @Nullable
    private List<String> chatRulesCited;

}
