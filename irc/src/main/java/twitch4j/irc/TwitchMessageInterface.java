package twitch4j.irc;

import lombok.Getter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import twitch4j.Configuration;
import twitch4j.common.events.EventManager;

import javax.annotation.Nullable;
import java.net.URI;

public class TwitchMessageInterface extends WebSocketClient {
	@Nullable
	private final Configuration configuration;
	private final MessageParser parser;

	public TwitchMessageInterface(Configuration configuration, EventManager eventManager) {
		super(URI.create("wss://irc-ws.chat.twitch.tv:443/"), new Draft_6455());
		this.configuration = configuration;
		this.parser = new MessageParser(this, eventManager);
	}

	public boolean ready() {
		return configuration.getBotCredentials() != null;
	}

	@Override
	public void connect() {
		if (ready()) super.connect();
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {

	}

	@Override
	public void onMessage(String message) {
		parser.handleMessage(message);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {

	}

	@Override
	public void onError(Exception ex) {

	}
}
