package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class ExtensionBitsProduct {

    /**
     * SKU of the Bits product. This is unique across all products that belong to an Extension.
     */
    private String sku;

    /**
     * Object containing cost information.
     */
    private Cost cost;

    /**
     * Indicates if the product is in development and not yet released for public use.
     */
    @Accessors(fluent = true)
    @JsonProperty("in_development")
    private Boolean isInDevelopment;

    /**
     * Name of the product to be displayed in the Extension.
     */
    private String displayName;

    /**
     * Expiration time for the product in RFC3339 format.
     */
    private Instant expiration;

    /**
     * Indicates if Bits product purchase events are broadcast to all instances of an Extension on a channel via the “onTransactionComplete” helper callback.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_broadcast")
    private Boolean isBroadcast;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Cost {

        /**
         * Number of Bits for which the product will be exchanged.
         */
        private Integer amount;

        /**
         * Cost type. The one valid value is "bits".
         */
        private String type;

    }

}
