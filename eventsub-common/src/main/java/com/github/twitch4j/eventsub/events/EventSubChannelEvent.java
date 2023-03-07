package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EventSubChannelEvent extends EventSubEvent {

    /**
     * The requested broadcaster ID.
     */
    @JsonAlias("broadcaster_id")
    private String broadcasterUserId;

    /**
     * The requested broadcaster display name.
     */
    @JsonAlias("broadcaster_name")
    private String broadcasterUserName;

    /**
     * The requested broadcaster login name.
     */
    @JsonAlias("broadcaster_login")
    private String broadcasterUserLogin;

}
