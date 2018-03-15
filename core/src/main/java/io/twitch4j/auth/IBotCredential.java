package io.twitch4j.auth;

public interface IBotCredential extends ICredential {
    default String getUsername() {
        return getUser().getUsername().toLowerCase();
    }

    default String getPassword() {
        return "oauth:" + getAccessToken();
    }
}
