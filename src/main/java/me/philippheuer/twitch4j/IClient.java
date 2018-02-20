package me.philippheuer.twitch4j;

import me.philippheuer.twitch4j.api.helix.IHelix;
import me.philippheuer.twitch4j.api.kraken.IKraken;
import me.philippheuer.twitch4j.auth.IManager;
import me.philippheuer.twitch4j.event.IDispatcher;
import me.philippheuer.twitch4j.irc.IMessageInterface;
import me.philippheuer.twitch4j.pubsub.IPubSub;
import me.philippheuer.twitch4j.utils.ISocket;

public interface IClient extends ISocket {
	IConfiguration getConfiguration();
	IHelix getHelixApi();
	@Deprecated
	IKraken getKrakenApi();
	IManager getCredentialManager();
	IDispatcher getDispatcher();
	IPubSub getPubSub();
	IMessageInterface getMessageInterface();
}
