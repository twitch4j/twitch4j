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
	DONATION_CREATE("donations.create");

    private String key;

    /**
     * Constructor
     * @param key
     */
    StreamlabsScopes(String key) {
        this.key = key;
    }

    /**
     * Combine <code>TwitchScopes</code> into a '+' separated <code>String</code>.
     * This is the required input format for twitch.tv
     *
     * @param scopes <code>TwitchScopes</code> to combine.
     * @return <code>String</code> representing '+' separated list of <code>TwitchScopes</code>
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
     * Convert the string representation of the Scope to the Enum.
     *
     * @param text Text representation of Enum value
     * @return Enum value that the text represents
     */
    public static StreamlabsScopes fromString(String text) {
        if (text == null) return null;
        for (StreamlabsScopes b : StreamlabsScopes.values()) {
            if (text.equalsIgnoreCase(b.key)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Get the identifier that twitch will recognize.
     *
     * @return A <code>String</code> identifier
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}
