package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BanUsersInput {

    /**
     * The list of users to ban or put in a timeout.
     * You may specify a maximum of 100 users.
     */
    @Singular
    @JsonProperty("data")
    private List<BanUserInput> actions;

    /**
     * Simplified Constructor
     *
     * @param userIds  The ids of the users to be banned or timed out.
     * @param reason   The reason for the mod action.
     * @param duration Seconds of the timeout period, or null for a ban.
     */
    public BanUsersInput(Collection<String> userIds, String reason, Integer duration) {
        List<BanUserInput> actions = new ArrayList<>(userIds.size());
        for (String userId : userIds) {
            actions.add(new BanUserInput(userId, reason, duration));
        }
        this.actions = actions;
    }

    /**
     * Simplified Constructor
     *
     * @param userIds The ids of the users to be banned.
     * @param reason  The reason for the ban.
     */
    public BanUsersInput(Collection<String> userIds, String reason) {
        this(userIds, reason, null);
    }

}
