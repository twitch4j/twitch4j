package me.philippheuer.twitch4j.message.pubsub;

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

}
