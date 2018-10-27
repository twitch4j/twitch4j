package com.github.twitch4j.helix;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;

public class TestUtils {

    public TwitchHelix getTwitchHelixClient() {
        TwitchHelix client = TwitchHelixBuilder.builder().build();
        return client;
    }

    public OAuth2Credential getCredential() {
        return new OAuth2Credential("twitch", null, System.getenv("TWITCH_AUTH_TOKEN"));
    }

}
