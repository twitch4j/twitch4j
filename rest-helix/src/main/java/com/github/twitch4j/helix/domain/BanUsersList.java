package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class BanUsersList {

    /**
     * The list of users you successfully banned or put in a timeout.
     * The users are in the same order as you specified them in the request.
     */
    @JsonProperty("data")
    private List<BanUsersResult> successfulActions;

}
