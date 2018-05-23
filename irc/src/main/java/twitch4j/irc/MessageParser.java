package twitch4j.irc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import twitch4j.common.events.EventManager;

@Getter
@RequiredArgsConstructor
public class MessageParser {
	private final WebSocketClient webSocketClient;
	private final EventManager eventManager;

	public void handleMessage(String rawMessage) {
		if (rawMessage.contains(System.lineSeparator())) {
			for (String rawMsg : rawMessage.split(System.lineSeparator())) {
				handleMessage(rawMsg);
			}
		} else {
			parseAndDispatch(rawMessage);
		}
	}

	private void parseAndDispatch(String rawMessage) {

	}
}
