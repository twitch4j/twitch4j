package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Additional information about a product acquired via a Twitch Extension Bits transaction.
 */
@Data
@Setter(AccessLevel.PRIVATE)
public class Product {

    /**
     * Product name.
     */
    private String name;

    /**
     * Bits involved in the transaction.
     */
    private Integer bits;

    /**
     * Unique identifier for the product acquired.
     */
    private String sku;

    /**
     * Whether the product is in development.
     * When true, bits is zero.
     */
    @Accessors(fluent = true)
    @JsonProperty("in_development")
    private Boolean isInDevelopment;

}
