package io.twitch4j;

import io.twitch4j.auth.IBotCredential;
import io.twitch4j.auth.ICredentialStore;
import io.twitch4j.impl.TwitchClientImpl;
import io.twitch4j.impl.auth.FileCredentialStore;
import io.twitch4j.utils.Util;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

@Getter
@Wither
@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBuilder {
    private String clientId;
    private String clientSecret;
    private String userAgent = Util.getDefaultUserAgent();
    private boolean savingCredentials = false;
    private Function<ITwitchClient, ICredentialStore> credentialStore = client -> new FileCredentialStore(client, new File("credentials.json"));

    private IBotCredential botCredential;

    private Set<Object> listeners = new LinkedHashSet<>();

    public static ClientBuilder newClient() {
        return new ClientBuilder();
    }

    public <T> ClientBuilder addListener(T listener) {
        listeners.add(listener);
        return this;
    }

    public <T> ClientBuilder addListeners(T... listeners) {
        addListeners(Arrays.asList(listeners));
        return this;
    }

    public <T> ClientBuilder addListeners(Collection<T> listeners) {
        this.listeners.addAll(listeners);
        return this;
    }

    public <T> ClientBuilder removeListener(T listener) {
        listeners.remove(listener);
        return this;
    }

    public <T> ClientBuilder removeListeners(T... listeners) {
        removeListeners(Arrays.asList(listeners));
        return this;
    }

    public <T> ClientBuilder removeListeners(Collection<T> listeners) {
        this.listeners.removeAll(listeners);
        return this;
    }

    public ITwitchClient build() {
        return new TwitchClientImpl(this);
    }

    public ITwitchClient connect() {
        ITwitchClient client = build();
        client.connect();
        return client;
    }
}
