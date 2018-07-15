package me.philippheuer.twitch4j.enums;

/**
 * Enum for Twitch SubPlans
 */
public enum SubPlan {
	/**
	 * Unknown
	 */
	UNKNOWN("Unknown"),

	/**
	 * Twitch Prime
	 */
	PRIME("Prime"),

	/**
	 * Sub Tier 1
	 */
	TIER_1("1000"),

	/**
	 * Sub Tier 2
	 */
	TIER_2("2000"),

	/**
	 * Sub Tier 3
	 */
	TIER_3("3000");

	/**
	 * Scope Key
	 */
	private String key;

	/**
	 * Class Constructor
	 *
	 * @param key
	 */
	SubPlan(String key) {
		this.key = key;
	}

	/**
	 * Gets the enum from the string representation.
	 *
	 * @param text Text representation of Enum value
	 * @return Enum value that the text represents
	 */
	public static SubPlan fromString(String text) {
		if (text == null) {
			return null;
		}

		for (SubPlan b : SubPlan.values()) {
			if (text.equalsIgnoreCase(b.key)) {
				return b;
			}
		}

		return null;
	}

	/**
	 * Get the identifier that oauth will recognize.
	 *
	 * @return A <code>String</code> identifier of the scope.
	 */
	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return key;
	}
}
