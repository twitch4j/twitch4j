package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * @return the cursor to specify in the next call to obtain the next page of results, in an optional wrapper
     * @implNote Pagination for this endpoint is currently <a href="https://github.com/twitchdev/issues/issues/540">broken</a>
     * in <a href="https://github.com/twitchdev/issues/issues/539">multiple</a> <a href="https://github.com/twitchdev/issues/issues/541">respects</a>
     */
    @JsonIgnore
    public Optional<String> getCursor() {
        // Currently, pagination is returned directly as a string, which departs from all of the other helix endpoints
        // In the future, Twitch may fix the above inconsistency and return an object that contains a string
        return Optional.ofNullable(pagination instanceof Map ? ((Map<?, ?>) pagination).get("cursor") : pagination)
            .filter(c -> c instanceof String)
            .map(c -> (String) c)
            .filter(StringUtils::isNotBlank);
    }

}
