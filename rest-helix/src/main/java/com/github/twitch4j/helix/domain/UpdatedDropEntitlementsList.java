package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdatedDropEntitlementsList {

    /**
     * The entitlement update statuses.
     */
    private List<UpdatedDropEntitlements> data;

    @ToString.Exclude
    @Getter(lazy = true)
    private final Map<UpdateEntitlementStatus, Collection<String>> transformedData = transform(getData());

    private static Map<UpdateEntitlementStatus, Collection<String>> transform(Collection<UpdatedDropEntitlements> rawData) {
        final Map<UpdateEntitlementStatus, Collection<String>> map = new EnumMap<>(UpdateEntitlementStatus.class);

        for (UpdatedDropEntitlements updated : rawData) {
            map.merge(updated.getStatus(), new HashSet<>(updated.getIds()), (c1, c2) -> {
                c1.addAll(c2);
                return c1;
            });
        }

        return Collections.unmodifiableMap(map);
    }

}
