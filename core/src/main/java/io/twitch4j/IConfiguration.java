package io.twitch4j;

import io.twitch4j.auth.IBotCredential;
import io.twitch4j.impl.ConfigurationBuilder;

public interface IConfiguration {
    String getClientId();

    String getClientSecret();

    String getUserAgent();

    IBotCredential getBotCredential();

    class Builder extends ConfigurationBuilder {
    }
}
