package com.github.twitch4j.helix.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DropsEntitlementList {

    /**
     * The entitlements
     */
    private List<DropsEntitlement> data;

    /**
     * If provided, is the key used to fetch the next page of data.
     * If not provided, the current response is the last page of data available.
     */
    private Object pagination;

    /**
     * @return the cursor from the pagination object, in an optional wrapper
     */
    public Optional<String> getPaginationCursor() {
        // The example response contained in the official documentation claims that the pagination field in the JSON
        // is simply a string, deviating from the rest of the helix endpoints which use an object that contains a string.
        // Since this may be an error that we are unable to test, we provide this code to be agnostic to both approaches.
        return Optional.ofNullable(pagination instanceof Map ? ((Map<?, ?>) pagination).get("cursor") : pagination)
            .filter(c -> c instanceof String)
            .map(c -> (String) c)
            .filter(StringUtils::isNotBlank);
    }

}
