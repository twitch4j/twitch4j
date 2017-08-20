package me.philippheuer.twitch4j.message;


import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.message.irc.Chat;
import me.philippheuer.twitch4j.message.pubsub.TwitchPubSub;

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
	public void sendMessage(String channel, String message) {}
	public void sendPrivateMessage(String username, String message) {
		// using pubsub endpoint to send private message
	}

	public void joinChannel(String channel) {

	}
	public void partChannel() {

	}
}
