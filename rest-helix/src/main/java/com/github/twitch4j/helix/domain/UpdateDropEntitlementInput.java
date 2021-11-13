package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateDropEntitlementInput {

    /**
     * The fulfillment status to assign to each of the enclosed entitlement ids.
     */
    private DropFulfillmentStatus fulfillmentStatus;

    /**
     * The unique identifiers of the entitlements to update.
     */
    @Singular
    private List<String> entitlementIds;

}
