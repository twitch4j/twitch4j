package com.github.twitch4j.eventsub.domain.moderation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Warning extends UserTarget {

    /**
     * Reason given for the warning.
     */
    @Nullable
    private String reason;

    /**
     * Chat rules cited for the warning.
     */
    @Nullable
    private List<String> chatRulesCited;

}
