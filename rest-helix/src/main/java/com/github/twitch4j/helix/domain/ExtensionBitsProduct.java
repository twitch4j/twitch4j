package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtensionBitsProduct {

    /**
     * SKU of the Bits product.
     * <p>
     * This is unique across all products that belong to an Extension.
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

    @With
    @Data
    @Setter(AccessLevel.PRIVATE)
    @Builder(toBuilder = true)
    @Jacksonized
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Cost {

        /**
         * Number of Bits for which the product will be exchanged.
         */
        private Integer amount;

        /**
         * Cost type.
         * <p>
         * The one valid value is "bits".
         */
        @Builder.Default
        private String type = "bits";

    }

}
