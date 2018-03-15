package io.twitch4j.api.model;

import io.twitch4j.ITwitchClient;
import io.twitch4j.auth.ICredential;

import java.util.Optional;

public abstract class Model {
    public abstract ITwitchClient getClient();

    public abstract Optional<ICredential> getCredential();
}
