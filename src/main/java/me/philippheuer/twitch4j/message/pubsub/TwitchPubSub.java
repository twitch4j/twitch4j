package me.philippheuer.twitch4j.message.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jcabi.log.Logger;
import com.neovisionaries.ws.client.*;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.model.Channel;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class TwitchPubSub {
	/**
	 * Connection statement
	 */
	public enum Connection {
		DISCONNECTING,
		RECONNECTING,
		DISCONNECTED,
		CONNECTING,
		CONNECTED
	}

	/**
	 * List fo subscribed channels
	 */
	private final List<Channel> registeredChannels = new ArrayList<Channel>();
	/**
	 * Timer for Scheduler Tasks
	 */
	private final Timer timer = new Timer();
	/**
	 * Holds the API Instance
	 */
	private final TwitchClient twitchClient;
	/**
	 * WebSocketFactory
	 */
	private WebSocket webSocket;
	/**
	 * Connection Statement
	 */
	private Connection connection = Connection.DISCONNECTED;

	/**
	 * Class Constructor
	 *
	 * @param twitchClient TwitchClient is the core class for all api operations.
	 */
	public TwitchPubSub(TwitchClient twitchClient) {
		this.twitchClient = twitchClient;
		// Connect to twitch pubsub server
		try {
			webSocket = new WebSocketFactory().createSocket(Endpoints.PUBSUB.getURL());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Register Listener
		webSocket.addListener(new WebSocketAdapter() {
			@Override
			public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
				registerChannels();
				setConnection(Connection.CONNECTED);
			}

			@Override
			public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
				Logger.info(this, "Connection to Twitch PubSub Closed by Server [%s]", Endpoints.PUBSUB.getURL());
				if (!getConnection().equals(Connection.DISCONNECTING)) {
					connect();
				} else setConnection(Connection.DISCONNECTED);
			}

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
							reconnect();
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
		});
	}

	public void connect() {
		if (connection.equals(Connection.DISCONNECTED)) {
			Logger.info(this, "Connecting to Twitch PubSub: [%s]", Endpoints.PUBSUB.getURL());

			try {
				getWebSocket().connect();
				connection = Connection.CONNECTING;
				// Schedule Tasks
				scheduleTasks();
			} catch (Exception ex) {
				Logger.error(this, "Connection to Twitch PubSub failed: [%s]", ex.getMessage());
			}
		} else {
			Logger.warn(this, "Cannot connecting to Twitch PubSub: is already [%s].", connection.name().toUpperCase());
		}
	}

	public void reconnect() {
		setConnection(Connection.RECONNECTING);
		disconnect(false);
		scheduleTasks();
	}

	private void scheduleTasks() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// Prepare JSON Ping Message
				try {
					ObjectMapper mapper = new ObjectMapper();
					ObjectNode objectNode = mapper.createObjectNode();
					objectNode.put("type", "PING");

					webSocket.sendText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode));

					Logger.debug(this, "Send Ping to Twitch PubSub. (Keep-Connection-Alive)");
				} catch (Exception ex) {
					Logger.error(this, "Failed to Ping Twitch PubSub. (%s)", ex.getMessage());
					reconnect();
				}

			}
		}, 7000, 282000);
	}

	/**
	 * Disconnect
	 * @param forceDisconnect forcing disconnection
	 */
	public void disconnect(boolean forceDisconnect) {
		if (forceDisconnect) connection = Connection.DISCONNECTING;
		webSocket.disconnect();
		cancelTasks();
	}

	/**
	 * Disconnect with forcing disconnection
	 */
	public void disconnect() {
		disconnect(true);
	}

	/**
	 * Purge the current tasks to prepare for a reconnect.
	 */
	private void cancelTasks() {
		timer.cancel();
		timer.purge();
	}

	/**
	 * Subscribe to Bits Topic
	 *
	 * @param channel Channel
	 * @see Channel
	 */
	public void addChannel(Channel channel) {
		// Validate Arguments
		Assert.isTrue(channel.getTwitchCredential().isPresent(), "You need to provide twitch credentials with an oauth token to use pubsub.");

		// Add if new
		if (!getRegisteredChannels().contains(channel)) {
			getRegisteredChannels().add(channel);
		}

		if (checkEndpointStatus()) {
			subscribeToBitsTopic(channel);
		}
	}

	/**
	 * Subscribe to Bits Topic
	 */
	private void subscribeToBitsTopic(Channel channel) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();

		objectNode.put("type", "LISTEN");
		objectNode.put("nonce", channel.getId() + ".pubsub.bitevents");

		ObjectNode dataMap = objectNode.putObject("data");
		dataMap.put("auth_token", channel.getTwitchCredential().get().getToken());
		ArrayNode topicArray = dataMap.putArray("topics");
		topicArray.add("channel-bitsevents." + channel.getId());

		Logger.error(this, "Sending BitEvents-Subscription to PubSub: %s", objectNode.toString());

		webSocket.sendText(objectNode.toString());
	}

	/**
	 * Method: Check PubSub Socket Status
	 *
	 * @return True, if the socket is connected. False, if there are problems with the pubsub endpoint.
	 */
	public Boolean checkEndpointStatus() {
		// WebSocket needs to be open
		if (getWebSocket() == null || !getWebSocket().getState().equals(WebSocketState.OPEN)) {
			return false;
		}

		// TODO

		return true;
	}

	/**
	 * register channel for {@link TwitchPubSubListener}
	 */
	void registerChannels() {
		for (Channel channel : getRegisteredChannels()) {
			subscribeToBitsTopic(channel);
		}
	}
}
