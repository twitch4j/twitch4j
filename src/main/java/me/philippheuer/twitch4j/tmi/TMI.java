package me.philippheuer.twitch4j.tmi;


import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.tmi.chat.WsIrcClient;
import me.philippheuer.twitch4j.tmi.pubsub.TwitchPubSub;

@Getter
@Setter
public class TMI {

	private final TwitchClient twitchClient;
	private final WsIrcClient twitchChat;
	private final TwitchPubSub twitchPubSub;

	public TMI(TwitchClient client) {
		this.twitchClient = client;
		this.twitchChat = new WsIrcClient(client);
		this.twitchPubSub = new TwitchPubSub(client);
	}
}
