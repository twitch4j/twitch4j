package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * The list of users that weren't banned.
     * The string is in the form: {@code &lt;user_id&gt;: &lt;error message&gt;}.
     * The list is empty if all users were successfully banned.
     */
    private List<String> errors;

    /**
     * @return whether there were users in the call that were not banned or timed-out as requested
     */
    @JsonIgnore
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    /**
     * @return a mapping of user id's to the reason why the ban or timeout failed
     */
    public Map<String, String> getErrorReasonsByUserId() {
        if (!hasErrors()) return Collections.emptyMap();
        final Map<String, String> parsed = new LinkedHashMap<>();
        for (String error : errors) {
            String[] parts = StringUtils.split(error, ": ", 2);
            parsed.put(parts[0].trim(), parts[1].trim());
        }
        return Collections.unmodifiableMap(parsed);
    }

}
