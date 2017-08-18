package me.philippheuer.twitch4j.tmi.chat;

import com.neovisionaries.ws.client.WebSocket;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Getter
@Setter
public class WsIrcParser {
	/**
	 * Raw Message
	 */
	private final Message message;
	private final TwitchClient twitchClient;
	private final WebSocket webSocket;

	/**
	 *
	 * @param client
	 * @param ws
	 * @param message
	 */
	WsIrcParser(TwitchClient client, WebSocket ws, String message) {
		this.twitchClient = client;
		this.webSocket = ws;
		this.message = new Message(message);
	}

	static void initialize(WebSocket ws, TwitchClient client) {
		ws.sendText("CAP REQ :twitch.tv/membership");
		ws.sendText("CAP REQ :twitch.tv/tags");
		ws.sendText("CAP REQ :twitch.tv/commands");

		Optional<OAuthCredential> twitchCredential = client.getCredentialManager().getTwitchCredentialsForIRC();

		ws.sendText("PASS oauth:" + twitchCredential.get().getToken());
		ws.sendText("NICK " + twitchCredential.get().getUserName());
	}

	public boolean pingRecived() {
		return message.contains("PING");
	}

	public void sendRawMessage(String message) {
		getWebSocket().sendText(message);
	}

	class Message {

		private final String rawMessage;

		Message(String message) {
			this.rawMessage = message;
		}

		private MessageParsed parse() {
			return new MessageParsed(rawMessage);
		}

		public String toString() {
			return rawMessage;
		}

		private boolean contains(CharSequence message) {
			return toString().contains(message);
		}
	}

	@Getter
	class MessageParsed {
		HashMap<String, ?> tags = new HashMap<>();
		ArrayList<String> params = new ArrayList<>();

		MessageParsed(String rawMessage) {

		}
	}

}
