package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
public class ExtensionLiveChannelsList {

    /**
     * One page of live channels that have installed or activated a specific Extension.
     */
    @JsonProperty("data")
    private List<ExtensionLiveChannel> channels;

    @Getter(AccessLevel.PRIVATE)
    private Object pagination;

    /**
     * @return the cursor to specify in the next call to obtain the next page of results
     * @implNote Pagination for this endpoint is currently <a href="https://github.com/twitchdev/issues/issues/540">broken</a>
     * in <a href="https://github.com/twitchdev/issues/issues/539">multiple</a> <a href="https://github.com/twitchdev/issues/issues/541">respects</a>
     */
    @JsonIgnore
    public String getCursor() {
        if (pagination instanceof String) {
            // Currently, pagination is returned directly as a string, which departs from all of the other helix endpoints
            return (String) pagination;
        }

        if (pagination instanceof Map) {
            // In the future, Twitch may fix the above inconsistency and return an object that contains a string
            return (String) ((Map<?, ?>) pagination).get("cursor");
        }

        return pagination != null ? pagination.toString() : null;
    }

}
