package com.github.twitch4j.common;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;

public class TwitchTestUtils {

    /**
     * Gets the OAuth Credential for integration tests
     *
     * @return OAuth2Credential
     */
    public static OAuth2Credential getCredential() {
        return new OAuth2Credential("twitch", System.getenv("TWITCH_AUTH_TOKEN"));
    }

    /**
     * Gets the OAuth Credential for integration tests
     *
     * @return OAuth2Credential
     */
    public static OAuth2Credential getGraphQLCredential() {
        return new OAuth2Credential("twitch", System.getenv("TWITCH_AUTH_GQL_TOKEN"));
    }

}
