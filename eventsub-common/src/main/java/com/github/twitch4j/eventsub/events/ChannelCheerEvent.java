package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.common.util.TwitchUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelCheerEvent extends EventSubUserChannelEvent {

    /**
     * Whether the user cheered anonymously or not.
     */
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
    @JsonIgnore
    public String getUserId() {
        return isAnonymous ? TwitchUtils.ANONYMOUS_CHEERER.getId() : super.getUserId();
    }

    @Override
    @JsonIgnore
    public String getUserName() {
        return isAnonymous ? TwitchUtils.ANONYMOUS_CHEERER.getName() : super.getUserName();
    }

}
