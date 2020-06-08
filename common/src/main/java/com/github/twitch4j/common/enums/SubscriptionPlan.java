package com.github.twitch4j.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Enumeric Twitch Subscription Plan
 */
@RequiredArgsConstructor
public enum SubscriptionPlan {
    NONE("none"),
    TWITCH_PRIME("Prime"),
    TIER1("1000"),
    TIER2("2000"),
    TIER3("3000");

    @Getter
    @Accessors(fluent = true)
    private final String ordinalName;

    private static final SubscriptionPlan[] VALUES = values();

    @JsonCreator
    public static SubscriptionPlan fromString(String plan) {
        if (plan != null) {
            for (SubscriptionPlan subPlan : VALUES) {
                if (plan.equalsIgnoreCase(subPlan.ordinalName)) {
                    return subPlan;
                }
            }
        }

        return NONE;
    }

    @Override
    public String toString() {
        return ordinalName;
    }
}
