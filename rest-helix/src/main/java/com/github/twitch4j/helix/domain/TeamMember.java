package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TeamMember extends Team {

    /**
     * User ID of the broadcaster.
     */
    private String broadcasterId;

    /**
     * Display name of the broadcaster.
     */
    private String broadcasterName;

    /**
     * Login of the broadcaster.
     */
    private String broadcasterLogin;

}
