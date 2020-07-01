package com.github.twitch4j.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Enumeric Twitch Subscription Type
 */
public enum SubscriptionType {
    SUB,
    RESUB,
    SUB_GIFT,
    ANON_SUB_GIFT,
    RESUB_GIFT,
    ANON_RESUB_GIFT;

    @Getter
    @Accessors(fluent = true)
    private final String ordinalName;

    SubscriptionType() {
        this.ordinalName = this.name().toLowerCase().replace("_", "");
    }

    private static final SubscriptionType[] VALUES = values();

    @JsonCreator
    public static SubscriptionType fromString(String type) {
        if (type != null) {
            for (SubscriptionType subType : VALUES) {
                if (type.equalsIgnoreCase(subType.ordinalName)) {
                    return subType;
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return ordinalName;
    }
}
