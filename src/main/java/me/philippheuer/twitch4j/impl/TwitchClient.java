package me.philippheuer.twitch4j.impl;

import lombok.Getter;
import me.philippheuer.twitch4j.api.TwitchApi;
import me.philippheuer.twitch4j.connect.TwitchServiceProvider;
import me.philippheuer.twitch4j.models.IApplication;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.api.model.ICredential;
import me.philippheuer.twitch4j.impl.pubsub.PubSub;
import me.philippheuer.twitch4j.impl.tmi.MessageInterface;
import me.philippheuer.twitch4j.pubsub.IPubSub;
import me.philippheuer.twitch4j.tmi.IMessageInterface;

@Getter
public class TwitchClient<T extends org.springframework.social.oauth2.AbstractOAuth2ServiceProvider> implements IClient {

    private final IMessageInterface messageInterface = new MessageInterface(this);
    private final IPubSub pubSub = new PubSub(this);

    private final IApplication application;
    private final TwitchServiceProvider provider;

    public TwitchClient(IApplication application) {
    	this.application = application;
		this.provider = new TwitchServiceProvider(application.getClientId(), application.getClientSecret());
    }

    @Override
    public TwitchApi getApi() {
        return provider.getApi(null);
    }

    @Override
    public TwitchApi getApi(ICredential credential) {
        return provider.getApi(credential.getAccessToken());
    }

	@Override
	public void connect() {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public void reconnect() {

	}
}
