package io.twitch4j.impl;

import io.twitch4j.ClientBuilder;
import io.twitch4j.IConfiguration;
import io.twitch4j.ITwitchClient;
import io.twitch4j.api.helix.IHelix;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.auth.IBotCredential;
import io.twitch4j.auth.ICredentialStore;
import io.twitch4j.auth.IManager;
import io.twitch4j.events.IDispatcher;
import io.twitch4j.impl.api.helix.HelixImpl;
import io.twitch4j.impl.api.kraken.KrakenImpl;
import io.twitch4j.impl.auth.CredentialManagerImpl;
import io.twitch4j.impl.events.EventDispatcherImpl;
import io.twitch4j.impl.pubsub.PubSubImpl;
import io.twitch4j.impl.tmi.MessageInterfaceImpl;
import io.twitch4j.pubsub.IPubSub;
import io.twitch4j.tmi.IMessageInterface;
import io.vertx.core.Vertx;
import lombok.Getter;
import org.apache.commons.lang3.Validate;

@Getter
public class TwitchClientImpl implements ITwitchClient {
    private final IKraken krakenApi = new KrakenImpl(this);
    private final IHelix helixApi = new HelixImpl(this);
    private final IDispatcher dispatcher = new EventDispatcherImpl(this);
    private final IManager credentialManager;
    private final IPubSub pubSub = new PubSubImpl(this);
    private final IMessageInterface messageInterface = new MessageInterfaceImpl(this);
    private final Vertx vertx = Vertx.vertx();
    private IConfiguration configuration;

    public TwitchClientImpl(ClientBuilder clientBuilder) {
        this.configuration = new IConfiguration.Builder()
                .clientId(clientBuilder.getClientId())
                .clientSecret(clientBuilder.getClientSecret())
                .userAgent(clientBuilder.getUserAgent())
                .build();

        ICredentialStore credentialStore = null;
        if (clientBuilder.isSavingCredentials()) {
            credentialStore = clientBuilder.getCredentialStore().apply(this);
        }
        this.credentialManager = new CredentialManagerImpl(this, credentialStore);

        if (clientBuilder.getBotCredential() != null) {
            this.configuration = new IConfiguration.Builder()
                    .from(this.configuration)
                    .botCredential((IBotCredential) credentialManager.rebuild(clientBuilder.getBotCredential()))
                    .build();
        }
    }

    @Override
    public void connect() {
        Validate.notNull(configuration.getBotCredential(), "Required Bot Credentials to initialize client");
    }

    @Override
    public void disconnect() {
        Validate.notNull(configuration.getBotCredential(), "Required Bot Credentials to initialize client");

    }

    @Override
    public void reconnect() {
        Validate.notNull(configuration.getBotCredential(), "Required Bot Credentials to initialize client");
    }
}
