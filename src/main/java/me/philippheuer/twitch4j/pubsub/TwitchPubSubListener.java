package me.philippheuer.twitch4j.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.ws.client.*;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import com.jcabi.log.Logger;

import java.util.List;
import java.util.Map;

@Getter
@Setter
class TwitchPubSubListener extends WebSocketAdapter {

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	TwitchPubSubListener(TwitchClient twitchClient) { setTwitchClient(twitchClient); }

	/**
	 * Called after the opening handshake of the WebSocket connection succeeded.
	 *
	 * @param websocket The WebSsocket.
	 * @param headers   HTTP headers received from the server. Keys of the map are
	 *                  HTTP header names such as {@code "Sec-WebSocket-Accept"}.
	 *                  Note that the comparator used by the map is {@link
	 *                  String#CASE_INSENSITIVE_ORDER}.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
		twitchClient.getPubSub().registerChannels();
	}

	/**
	 * Called after the WebSocket connection was closed.
	 *
	 * @param websocket        The WebSocket.
	 * @param serverCloseFrame The <a href="https://tools.ietf.org/html/rfc6455#section-5.5.1"
	 *                         >close frame</a> which the server sent to this client.
	 *                         This may be {@code null}.
	 * @param clientCloseFrame The <a href="https://tools.ietf.org/html/rfc6455#section-5.5.1"
	 *                         >close frame</a> which this client sent to the server.
	 *                         This may be {@code null}.
	 * @param closedByServer   {@code true} if the closing handshake was started by the server.
	 *                         {@code false} if the closing handshake was started by the client.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
		Logger.info(this, "Connection to Twitch PubSub Closed by Server [%s]", getTwitchClient().getTwitchPubSubEndpoint());
		getTwitchClient().getPubSub().reconnect();
	}

	/**
	 * Called when a text message was received.
	 *
	 * @param websocket The WebSocket.
	 * @param text      The text message.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onTextMessage(WebSocket websocket, String text) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(text);

		if (jsonNode.has("type")) {
			switch (jsonNode.get("type").textValue().toLowerCase()) {
				case "pong":
					/*
					 * Handle: PONG
					 *  If a client does not receive a PONG message within 10 seconds
					 *  of issuing a PING command, it should reconnect to the server.
					 */
					Logger.debug(this, "Recieved PONG Response from Twitch PubSub.");
					break;
				case "reconnect":
					/*
					 * Handle: Reconnect
					 *  Clients may receive a RECONNECT message at any time.
					 *  This indicates that the server is about to restart (typically for maintenance)
					 *  and will disconnect the client within 30 seconds.
					 *  During this time, we recommend that clients reconnect to the server.
					 *  Otherwise, the client will be forcibly disconnected.
					 */
					Logger.debug(this, "Twitch PubSub is asking us to reconnect ...");
					getTwitchClient().getPubSub().reconnect();
					break;
				case "message":
					Logger.debug(this, "Recieved a message from Twitch PubSub: [%s]", text);
					// TODO: parse Messages
				default:
					return;
			}
		}

		if (jsonNode.has("error") && jsonNode.get("error") != null & jsonNode.get("error").textValue().length() > 0) {
			Logger.error(this, "Twitch PubSub encountered an error: %s", jsonNode.get("error").textValue());
		}
	}
}
