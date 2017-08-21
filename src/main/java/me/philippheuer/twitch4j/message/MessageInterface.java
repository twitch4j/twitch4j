package me.philippheuer.twitch4j.message;


import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.PubSubTopics;
import me.philippheuer.twitch4j.enums.TMIConnectionState;
import me.philippheuer.twitch4j.message.irc.TwitchChat;
import me.philippheuer.twitch4j.message.pubsub.TwitchPubSub;
import me.philippheuer.twitch4j.model.Channel;

import java.util.ArrayList;

@Getter
@Setter
public class MessageInterface {

	/**
	 * Twitch Client
	 */
	private final TwitchClient twitchClient;

	/**
	 * Twitch Chat Wrapper
	 */
	private final TwitchChat twitchChat;

	/**
	 * PubSub
	 */
	private final TwitchPubSub pubSub;
	// TODO: Listener for TMI
	// private final TMIListenere listener;

	/**
	 * Constructor
	 *
	 * @param client Twitch Client.
	 */
	public MessageInterface(TwitchClient client) {
		this.twitchClient = client;
		this.twitchChat = new TwitchChat(client);
		this.pubSub = new TwitchPubSub(client);
	}

	public void connect() {
		twitchChat.connect();
		pubSub.connect();
	}

	public void disconnect() {
		twitchChat.disconnect();
		pubSub.disconnect();
	}

	public void reconnect() {
		twitchChat.reconnect();
		pubSub.reconnect();
	}

	public boolean isJoined(String channel) {
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		// syncing channels
		if (twitchChat.getChannels().contains(ch) && !pubSub.getChannelList().containsKey(ch)) pubSub.getChannelList().put(ch, new ArrayList<PubSubTopics>());
		if (!twitchChat.getChannels().contains(ch) && pubSub.getChannelList().containsKey(ch)) twitchChat.getChannels().add(ch);

		return (twitchChat.getConnectionState().equals(TMIConnectionState.CONNECTED) && pubSub.getConnectionState().equals(TMIConnectionState.CONNECTED)) &&
				(twitchChat.getChannels().contains(ch) && pubSub.getChannelList().containsKey(ch));
	}

	public void sendMessage(String channel, String message) {
		twitchChat.sendMessage(channel, message);
	}

	public void sendPrivateMessage(String username, String message) {
		twitchChat.sendPrivateMessage(username, message);
	} // I don't know it will works.

	public void joinChannel(String channel) {
		twitchChat.joinChannel(channel);
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		pubSub.listenChannel(ch, true);
	}

	public void partChannel(String channel) {
		twitchChat.partChannel(channel);
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		pubSub.unlistenChannel(ch);
	}
}
