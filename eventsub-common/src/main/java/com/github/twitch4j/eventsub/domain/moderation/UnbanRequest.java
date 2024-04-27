package com.github.twitch4j.eventsub.domain.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UnbanRequest extends UserTarget {

    /**
     * The optional message included by the moderator explaining their approval or denial.
     */
    @Nullable
    private String moderatorMessage;

    /**
     * Whether the unban request was approved or denied.
     */
    @JsonProperty("is_approved")
    private boolean approved;

}
