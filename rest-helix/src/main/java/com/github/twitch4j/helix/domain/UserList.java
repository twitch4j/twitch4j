package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * User List
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserList {

    @JsonProperty("data")
    private List<User> users;

    private HelixPagination pagination;

}
