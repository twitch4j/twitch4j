package me.philippheuer.twitch4j.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jcabi.log.Logger;
import com.neovisionaries.ws.client.*;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.Channel;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class TwitchPubSub {

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
	private TwitchClient twitchClient;
	/**
	 * WebSocketFactory
	 */
	private WebSocket webSocket;

	/**
	 * Constructor
	 */
	public TwitchPubSub(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);

		// TODO PubSub needs to be implemented
		if (true) {
			return;
		}

		// Connect to twitch pubsub server
		try {
			setWebSocket(new WebSocketFactory().createSocket(getTwitchClient().getTwitchPubSubEndpoint()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Register Listener
		getWebSocket().addListener(new WebSocketAdapter() {
			/**
			 * Event: OnConnect (and Reconnect)
			 */
			@Override
			public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
				// Connected Successfully - Send Channel Subscriptions
				for (Channel channel : getRegisteredChannels()) {
					subscribeToBitsTopic(channel);
				}
			}

			/**
			 * Event: Message Received
			 */
			@Override
			public void onTextMessage(WebSocket ws, String message) {
				// Parse Message
				try {
					ObjectMapper mapper = new ObjectMapper();
					JsonNode jsonNode = mapper.readTree(message);

					// Only Handle Messages with a Type
					if (jsonNode.get("type") == null) {
						return;
					}

					/**
					 * Handle: PONG
					 *  If a client does not receive a PONG message within 10 seconds
					 *  of issuing a PING command, it should reconnect to the server.
					 */
					if (jsonNode.has("type") && jsonNode.get("type").textValue().equalsIgnoreCase("pong")) {
						Logger.debug(this, "Recieved PONG Response from Twitch PubSub.");
					}

					/**
					 * Handle: Reconnect
					 *  Clients may receive a RECONNECT message at any time.
					 *  This indicates that the server is about to restart (typically for maintenance)
					 *  and will disconnect the client within 30 seconds.
					 *  During this time, we recommend that clients reconnect to the server.
					 *  Otherwise, the client will be forcibly disconnected.
					 */
					if (jsonNode.has("type") && jsonNode.get("type").textValue().equalsIgnoreCase("reconnect")) {
						Logger.debug(this, "Twitch PubSub is asking us to reconnect ...");
						reconnect();
					}

					// Handle Error
					if (jsonNode.has("error") && jsonNode.get("error") != null & jsonNode.get("error").textValue().length() > 0) {
						Logger.error(this, "Twitch PubSub encountered an error: %s", jsonNode.get("error").textValue());
					}

					// Handle Message
					if (jsonNode.has("type") && jsonNode.get("type").textValue().equalsIgnoreCase("message")) {
						Logger.debug(this, "Recieved a message from Twitch PubSub: [%s]", message);
						// TODO: parse Messages
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			/**
			 * Event: ConnectionClosed
			 */
			@Override
			public void onDisconnected(WebSocket websocket,
									   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
									   boolean closedByServer) {

				Logger.info(this, "Connection to Twitch PubSub Closed by Server [%s]", getTwitchClient().getTwitchPubSubEndpoint());

				reconnect();
			}
		});

		// Connect
		connect();
	}

	private Boolean connect() {
		Logger.info(this, "Connecting to Twitch PubSub: [%s]",  getTwitchClient().getTwitchPubSubEndpoint());

		try {
			getWebSocket().connect();

			// Schedule Tasks
			scheduleTasks();
			return true;
		} catch (Exception ex) {
			Logger.error(this, "Connection to Twitch PubSub failed: [%s]",  ex.getMessage());
			return false;
		}
	}

	private void reconnect() {
		cancelTasks();
		if (connect()) {
			scheduleTasks();
		} else {
			// Reconnect failed, wait xxx
		}
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
	 * Purge the current tasks to prepare for a reconnect.
	 */
	private void cancelTasks() {
		timer.cancel();
		timer.purge();
	}

	/**
	 * Subscribe to Bits Topic
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
		dataMap.put("auth_token", channel.getTwitchCredential().get().getOAuthToken());
		ArrayNode topicArray = dataMap.putArray("topics");
		topicArray.add("channel-bitsevents." + channel.getId());

		Logger.error(this, "Sending BitEvents-Subscription to PubSub: %s", objectNode.toString());

		webSocket.sendText(objectNode.toString());
	}

	/**
	 * Method: Check PubSub Socket Status
	 */
	public Boolean checkEndpointStatus() {
		// WebSocket needs to be open
		if (getWebSocket() == null || !getWebSocket().getState().equals(WebSocketState.OPEN)) {
			return false;
		}

		// TODO

		return true;
	}
}
