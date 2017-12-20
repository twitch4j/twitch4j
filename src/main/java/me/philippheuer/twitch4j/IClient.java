package me.philippheuer.twitch4j;

import me.philippheuer.twitch4j.api.IApi;
import me.philippheuer.twitch4j.api.TwitchApi;
import me.philippheuer.twitch4j.api.model.ICredential;
import me.philippheuer.twitch4j.authorize.IManager;
import me.philippheuer.twitch4j.models.IApplication;
import me.philippheuer.twitch4j.pubsub.IPubSub;
import me.philippheuer.twitch4j.tmi.IMessageInterface;

public interface IClient {
    TwitchApi getApi();
	TwitchApi getApi(ICredential credential);
    IMessageInterface getMessageInterface();
    IPubSub getPubSub();

    IApplication getApplication();

    IManager getCredentialManager();

    void connect();
    void disconnect();
    void reconnect();
}
