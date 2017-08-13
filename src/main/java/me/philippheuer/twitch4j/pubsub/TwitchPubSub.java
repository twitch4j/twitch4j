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
	 * Class Constructor
	 *
	 * @param twitchClient TwitchClient is the core class for all api operations.
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
		getWebSocket().addListener(new TwitchPubSubListener(getTwitchClient()));

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

	void reconnect() {
		disconnect();
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
	 * Disconnect
	 */
	public void disconnect() {
		getWebSocket().disconnect();
		cancelTasks();
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
