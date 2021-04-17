package com.github.twitch4j.helix.domain;

import lombok.*;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Moderator {

    /** ID of the moderator. */
    @NonNull
    private String userId;

    /** Login of a moderator in the channel. */
    @NonNull
    private String userLogin;

    /** Display name of the moderator. */
    @NonNull
    private String userName;

}
