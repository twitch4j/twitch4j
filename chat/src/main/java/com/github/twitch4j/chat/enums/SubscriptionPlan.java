package com.github.twitch4j.chat.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Enumeric Twitch Subscription Plan
 */
@Deprecated
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

	public static SubscriptionPlan fromString(String plan) {
		if (plan != null) {
			for (SubscriptionPlan subPlan : values()) {
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
