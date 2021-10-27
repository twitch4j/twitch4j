package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdatedDropEntitlements {

    /**
     * Status code applied to a set of entitlements for the update operation that can be used to indicate partial success.
     */
    private UpdateEntitlementStatus status;

    /**
     * The unique identifiers of the entitlements for the specified status.
     */
    private List<String> ids;

}
