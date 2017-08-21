package me.philippheuer.twitch4j.message;


import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.message.irc.Chat;
import me.philippheuer.twitch4j.message.pubsub.TwitchPubSub;
import me.philippheuer.twitch4j.model.Channel;

@Getter
@Setter
public class MessageInterface {

	private final TwitchClient twitchClient;
	private final Chat chat;
	private final TwitchPubSub pubSub;

	public MessageInterface(TwitchClient client) {
		this.twitchClient = client;
		this.chat = new Chat(client);
		this.pubSub = new TwitchPubSub(client);
	}

	public void connect() {
		chat.connect();
		pubSub.connect();
	}
	public void disconnect() {
		chat.disconnect();
		pubSub.disconnect();
	}
	public void reconnect() {
		chat.reconnect();
		pubSub.reconnect();
	}

	public void sendMessage(String channel, String message) { chat.sendMessage(channel, message); }
	public void sendPrivateMessage(String username, String message) { chat.sendPrivateMessage(username, message); } // I don't know it will works.

	public void joinChannel(String channel) {
		chat.joinChannel(channel);
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		pubSub.listenChannel(ch, true);
	}
	public void partChannel(String channel) {
		chat.partChannel(channel);
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		pubSub.unlistenChannel(ch);
	}
}
