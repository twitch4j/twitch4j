package com.github.twitch4j.helix.domain;

public enum ExtensionState {

    IN_TEST,
    IN_REVIEW,
    REJECTED,
    APPROVED,
    RELEASED,
    DEPRECATED,
    PENDING_ACTION,
    ASSETS_UPLOADED,
    DELETED;

    @Override
    public String toString() {
        // convert enum to SentenceCase
        final char[] nameArray = this.name().toCharArray();
        final StringBuilder sb = new StringBuilder(nameArray.length);

        boolean capitalize = true; // capitalize first letter
        for (char c : nameArray) {
            if (c == '_') {
                capitalize = true; // capitalize next letter
            } else {
                sb.append(capitalize ? c : Character.toLowerCase(c));
                capitalize = false;
            }
        }

        return sb.toString();
    }
}
