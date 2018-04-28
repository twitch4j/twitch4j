package me.philippheuer.twitch4j.message;


import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.PubSubTopics;
import me.philippheuer.twitch4j.enums.TMIConnectionState;
import me.philippheuer.twitch4j.message.irc.ChannelCache;
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

	/**
	 * Connect to the TMI
	 */
	public void connect() {
		twitchChat.connect();
		pubSub.connect();
	}

	/**
	 * Connect from TMI
	 */
	public void disconnect() {
		twitchChat.disconnect();
		pubSub.disconnect();
	}

	/**
	 * Reconnect to the TMI
	 */
	public void reconnect() {
		twitchChat.reconnect();
		pubSub.reconnect();
	}

	/**
	 * Checking join channel status
	 * @param channel Channel name
	 * @return bot has been joined to this channel
	 */
	public boolean isJoined(String channel) {
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		// syncing channels
		if (twitchChat.getChannelCache().containsKey(channel) && !pubSub.getChannelList().containsKey(ch)) pubSub.getChannelList().put(ch, new ArrayList<PubSubTopics>());
		if (!twitchChat.getChannelCache().containsKey(channel) && pubSub.getChannelList().containsKey(ch)) twitchChat.getChannelCache().put(channel, new ChannelCache(twitchChat, channel));

		return (twitchChat.getConnectionState().equals(TMIConnectionState.CONNECTED) && pubSub.getConnectionState().equals(TMIConnectionState.CONNECTED)) &&
				(twitchChat.getChannelCache().containsKey(channel) && pubSub.getChannelList().containsKey(ch));
	}

	/**
	 * Sending message to the joined channel
	 * @param channel channel name
	 * @param message message
	 */
	public void sendMessage(String channel, String message) {
		twitchChat.sendMessage(channel, message);
	}

	/**
	 * sending private message
	 * @param username username
	 * @param message message
	 */
	public void sendPrivateMessage(String username, String message) {
		twitchChat.sendPrivateMessage(username, message);
	}

	/**
	 * Joining the channel
	 * @param channel channel name
	 */
	public void joinChannel(String channel) {
		twitchChat.joinChannel(channel);
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		pubSub.listenChannel(ch, true);
	}

	/**
	 * Leaving the channel
	 * @param channel channel name
	 * @deprecated See {@link #leaveChannel(String)}
	 */
	@Deprecated
	public void partChannel(String channel) {
		leaveChannel(channel);
	}

	/**
	 * Leaving the channel
	 * @param channel channel name
	 */
	public void leaveChannel(String channel){
		twitchChat.leaveChannel(channel);
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		pubSub.unlistenChannel(ch);
	}
}
