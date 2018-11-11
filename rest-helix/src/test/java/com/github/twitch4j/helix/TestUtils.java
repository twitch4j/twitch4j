package com.github.twitch4j.helix;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;

public class TestUtils {

    /**
     * Gets the OAuth Credential for integration tests
     *
     * @return OAuth2Credential
     */
    public static OAuth2Credential getCredential() {
        return new OAuth2Credential("twitch", System.getenv("TWITCH_AUTH_TOKEN"));
    }

    /**
     * Sleeps for x millis
     *
     * @param millis Millis
     */
    public static void sleepFor(Integer millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            // nothing
        }
    }

    /**
     * Gets a instance of the Helix Client
     * 
     * @return TwitchHelix
     */
    public static TwitchHelix getTwitchHelixClient() {
        TwitchHelix client = TwitchHelixBuilder.builder().build();
        return client;
    }

}
