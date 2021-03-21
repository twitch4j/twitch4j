package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class DropsEntitlementList {

    /**
     * The entitlements
     */
    private List<DropsEntitlement> data;

    private HelixPagination pagination;

    /**
     * If present, is the key used to fetch the next page of data.
     * If absent, the current response is the last page of data available.
     *
     * @return the cursor from the pagination object, in an optional wrapper
     * @deprecated in favor of {@link #getPagination()}
     */
    @Deprecated
    public Optional<String> getPaginationCursor() {
        return Optional.ofNullable(pagination).map(HelixPagination::getCursor);
    }

}
