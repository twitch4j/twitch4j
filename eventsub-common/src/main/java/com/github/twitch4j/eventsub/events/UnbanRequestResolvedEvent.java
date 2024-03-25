package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.UnbanRequestStatus;
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
public class UnbanRequestResolvedEvent extends EventSubModerationEvent {

    /**
     * The ID of the unban request.
     */
    @JsonProperty("id")
    private String requestId;

    /**
     * Resolution text supplied by the mod/broadcaster upon approval/denial of the request.
     */
    @Nullable
    private String resolutionText;

    /**
     * Dictates whether the unban request was approved or denied.
     */
    private UnbanRequestStatus status;

}
