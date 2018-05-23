package twitch4j.irc;

import lombok.Getter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import twitch4j.common.TwitchBotConfig;
import twitch4j.common.events.EventManager;

import javax.annotation.Nullable;
import java.net.URI;

public class TwitchMessageInterface extends WebSocketClient {
	@Nullable
	private final TwitchBotConfig botConfig;
	private final MessageParser parser;

	@Getter
	private boolean connected;

	public TwitchMessageInterface(TwitchBotConfig botConfig, EventManager eventManager) {
		super(URI.create("wss://irc-ws.chat.twitch.tv:443/"), new Draft_6455());
		this.botConfig = botConfig;
		this.parser = new MessageParser(eventManager);
	}

	public boolean ready() {
		return botConfig != null;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {

	}

	@Override
	public void onMessage(String message) {

	}

	@Override
	public void onClose(int code, String reason, boolean remote) {

	}

	@Override
	public void onError(Exception ex) {

	}
}
