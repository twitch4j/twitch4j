package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.util.TwitchUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelCheerEvent extends EventSubUserChannelEvent {

    /**
     * Whether the user cheered anonymously or not.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    /**
     * The message sent with the cheer.
     */
    private String message;

    /**
     * The number of bits cheered.
     */
    private Integer bits;

    @Override
    public String getUserId() {
        return isAnonymous ? TwitchUtils.ANONYMOUS_CHEERER.getId() : super.getUserId();
    }

    @Override
    public String getUserName() {
        return isAnonymous ? TwitchUtils.ANONYMOUS_CHEERER.getName() : super.getUserName();
    }

    @Override
    public String getUserLogin() {
        return isAnonymous ? TwitchUtils.ANONYMOUS_CHEERER.getName().toLowerCase() : super.getUserLogin();
    }

}
