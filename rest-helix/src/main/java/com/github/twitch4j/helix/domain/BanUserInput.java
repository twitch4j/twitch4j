package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BanUserInput {

    /**
     * The ID of the user to ban or put in a timeout.
     */
    private String userId;

    /**
     * The reason the user is being banned or put in a timeout.
     * The text is user defined and limited to a maximum of 500 characters.
     */
    private String reason;

    /**
     * Specify for a timeout, otherwise leave null for a ban.
     * <p>
     * To ban a user indefinitely, don’t include this field.
     * To put a user in a timeout, include this field and specify the timeout period, in seconds.
     * <p>
     * The minimum timeout is 1 second and the maximum is 1,209,600 seconds (2 weeks).
     * <p>
     * To end a user’s timeout early, set this field to 1, or send an Unban user request.
     */
    @Nullable
    private Integer duration;

}
