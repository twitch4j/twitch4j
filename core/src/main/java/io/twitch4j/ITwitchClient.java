package io.twitch4j;

import io.twitch4j.api.helix.IHelix;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.pubsub.IPubSub;
import io.twitch4j.tmi.IMessageInterface;

public interface ITwitchClient extends IClient {
    IConfiguration getConfiguration();

    @Deprecated
    IKraken getKrakenApi();

    IHelix getHelixApi();

    IPubSub getPubSub();

    IMessageInterface getMessageInterface();
}
