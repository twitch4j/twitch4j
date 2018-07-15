package me.philippheuer.twitch4j.message.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.enums.TMIConnectionState;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class PubSub {

	private static final Map<String, PubSubTopic> PRE_LISTEN = new ConcurrentHashMap<>(50);
	private static final Map<String, PubSubTopic> PRE_UNLISTEN = new ConcurrentHashMap<>();

	private final Set<PubSubTopic> topics = new LinkedHashSet<>(50);

	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * WebSocketFactory
	 */
	private WebSocket webSocket;

	/**
	 * The connection state
	 * Default: ({@link TMIConnectionState#DISCONNECTED})
	 */
	@Setter(AccessLevel.PRIVATE)
	private TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

	@Getter
	private final TwitchClient client;

	/**
	 * Timer for Scheduler Tasks
	 */
	private final Timer timer = new Timer();

	public PubSub(TwitchClient client) {
		SimpleModule simpleModule = new SimpleModule();

		this.client = client;
		this.mapper.registerModule(simpleModule);

		try {
			this.webSocket = new WebSocketFactory().createSocket(Endpoints.PUBSUB.getURL());
			this.webSocket.addListener(new WebSocketAdapter() {
				@Override
				public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
					setConnectionState(TMIConnectionState.CONNECTED);
					timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							// Prepare JSON Ping Message
							try {
								ObjectNode objectNode = mapper.createObjectNode();
								objectNode.put("type", "PING");

								webSocket.sendText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode));

								log.debug("Send Ping to Twitch PubSub. (Keep-Connection-Alive)");
							} catch (Exception ex) {
								log.error("Failed to Ping Twitch PubSub. (%s)", ex.getMessage());
								reconnect();
							}

						}
					}, 7000, 282000);
				}

				@Override
				public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
					if (closedByServer) {
						log.info("Connection to Twitch PubSub Closed by Server [%s]", Endpoints.PUBSUB.getURL());
					}

					if (!connectionState.equals(TMIConnectionState.DISCONNECTING)) {
						reconnect();
					} else {
						setConnectionState(TMIConnectionState.DISCONNECTED);
					}
				}

				@Override
				public void onTextMessage(WebSocket websocket, String text) throws Exception {
					JsonNode object = mapper.readTree(text);

					handle(object);
				}

				@Override
				public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
					log.error(cause.getMessage());
					log.error(ExceptionUtils.getStackTrace(cause));
				}
			});
		} catch (IOException e) {
			log.error("Cannot create socket", e);
		}
	}


	public boolean ready() {
		return !PRE_LISTEN.isEmpty();
	}

	public void connect() {

	}

	public void disconnect() {

	}

	public void reconnect() {

	}

	private void handle(JsonNode object) {
		if (object.has("type")) {
			switch (object.get("type").textValue().toLowerCase()) {
				case "pong":
					/*
					 * Handle: PONG
					 *  If a client does not receive a PONG message within 10 seconds
					 *  of issuing a PING command, it should reconnect to the server.
					 */
					log.debug("Recieved PONG Response from Twitch PubSub.");
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
					log.debug("Twitch PubSub is asking us to reconnect ...");
					reconnect();
					break;
				case "message":
					log.debug("Recieved a message from Twitch PubSub: %s", object.textValue());
//					handleMessage(object.get(""));
				default:
					break;
			}

			if (object.has("error") && object.get("error") != null & !object.get("error").textValue().isEmpty()) {
				switch (object.get("error").textValue().toUpperCase()) {
					case "ERR_BADMESSAGE":
						log.error("Twitch PubSub encountered an error: %s", "Wrong message format.");
						break;
					case "ERR_BADAUTH":
						log.error("Twitch PubSub encountered an error: %s", "Bad OAuth key or OAuth key doesn't have authorized scope to specify topic.");
						break;
					case "ERR_SERVER":
						log.error("Twitch PubSub encountered an error: %s", "Server Error.");
						break;
					case "ERR_BADTOPIC":
						log.error("Twitch PubSub encountered an error: %s", "Some topics is unknown.");
						break;
					default:
						return;
				}
			}
		}
	}
}
