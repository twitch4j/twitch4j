package me.philippheuer.twitch4j.streamlabs.enums;

/**
 * Streamlabs TwitchScopes
 */
public enum StreamlabsScopes {
    /**
     * Read access to donations.
     */
    DONATION_READ("donations.read"),

	/**
	 * Write access to donations.
	 */
	DONATION_CREATE("donations.create"),

	/**
	 * Write access to Alerts.
	 */
	ALERT_CREATE("alerts.create");

	/**
	 * The representation of the scope as text.
	 */
    private String key;

    /**
     * Class Constructor
	 *
     * @param key The string representation of the scope.
     */
    StreamlabsScopes(String key) {
        this.key = key;
    }

    /**
     * Combines scopes into a '+' separated <code>String</code>.
     * This is the required input format for streamlabs
     *
     * @param scopes {@link StreamlabsScopes} to combine.
     * @return <code>String</code> representing '+' separated list of the enum values.
     */
    public static String join(StreamlabsScopes... scopes) {
        if (scopes == null) return "";
        StringBuilder sb = new StringBuilder();
        for (StreamlabsScopes scope : scopes) {
            sb.append(scope.getKey()).append("+");
        }
        return sb.toString();
    }

    /**
	 * Gets the enum from the string representation of the scope.
     *
     * @param text Text representation of Enum value
     * @return Enum value that the text represents
     */
    public static StreamlabsScopes fromString(String text) {
		if (text == null) {
			return null;
		}

		for (StreamlabsScopes b : StreamlabsScopes.values()) {
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
