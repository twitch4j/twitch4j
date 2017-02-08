package me.philippheuer.twitch4j.pubsub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.*;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.model.Channel;

@Getter
@Setter
public class TwitchPubSub {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(TwitchPubSub.class);

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * WebSocketFactory
	 */
	private WebSocket webSocket;

	/**
	 * List fo subscribed channels
	 */
	private final List<Channel> registeredChannels = new ArrayList<Channel>();

	/**
	 * Timer for Scheduler Tasks
	 */
	private final Timer timer = new Timer();

	/**
	 * Constructor
	 */
	public TwitchPubSub(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);

		// TODO PubSub needs to be implemented
		if(true) {
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
	        	for(Channel channel : getRegisteredChannels()) {
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
                    if(jsonNode.get("type") == null) {
                    	return;
                    }

                    /**
                     * Handle: PONG
                     *  If a client does not receive a PONG message within 10 seconds
                     *  of issuing a PING command, it should reconnect to the server.
                     */
                    if(jsonNode.has("type") && jsonNode.get("type").textValue().equalsIgnoreCase("pong")) {
                    	logger.debug("Recieved PONG Response from Twitch PubSub.");
                    }

                    /**
                     * Handle: Reconnect
                     *  Clients may receive a RECONNECT message at any time.
                     *  This indicates that the server is about to restart (typically for maintenance)
                     *  and will disconnect the client within 30 seconds.
                     *  During this time, we recommend that clients reconnect to the server.
                     *  Otherwise, the client will be forcibly disconnected.
                     */
                    if(jsonNode.has("type") && jsonNode.get("type").textValue().equalsIgnoreCase("reconnect")) {
                    	logger.debug("Twitch PubSub is forcing us to reconnect ...");
                    	reconnect();
                    }

                    // Handle Error
                    if(jsonNode.has("error") && jsonNode.get("error") != null & jsonNode.get("error").textValue().length() > 0) {
                    	logger.debug(String.format("Twitch PubSub encountered an error: %s", jsonNode.get("error").textValue()));
                    }

                    // Handle Message
                    if(jsonNode.has("type") && jsonNode.get("type").textValue().equalsIgnoreCase("message")) {
                    	logger.debug(String.format("Recieved a message from Twitch PubSub [%s]", message));
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
            	logger.info(String.format("Connection to Twitch PubSub Closed by Server [%s]", getTwitchClient().getTwitchPubSubEndpoint()));
            	reconnect();
            }
        });

		// Connect
		connect();
	}

	private Boolean connect() {
		logger.info(String.format("Connecting to Twitch PubSub [%s]", getTwitchClient().getTwitchPubSubEndpoint()));

        try {
            getWebSocket().connect();

            // Schedule Tasks
    		scheduleTasks();
            return true;
        } catch (Exception ex) {
        	logger.error(String.format("Connection to Twitch PubSub [%s] Failed: %s", getTwitchClient().getTwitchPubSubEndpoint(), ex.getMessage()));
            return false;
        }
	}

	private void reconnect() {
		logger.info(String.format("Reconnecting to Twitch PubSub [%s]", getTwitchClient().getTwitchPubSubEndpoint()));

		cancelTasks();
		if(connect()) {
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
					logger.debug("Send Ping to Twitch PubSub. (Keep-Connection-Alive)");
				} catch (Exception ex) {
					logger.error("Failed to Ping Twitch PubSub. (Connection Lost)");
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
		if(!getRegisteredChannels().contains(channel)) {
			getRegisteredChannels().add(channel);
		}

		if(checkEndpointStatus()) {
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
		objectNode.put("nonce", channel.getId()+".pubsub.bitevents");

		ObjectNode dataMap = objectNode.putObject("data");
		dataMap.put("auth_token", channel.getTwitchCredential().get().getOAuthToken());
		ArrayNode topicArray = dataMap.putArray("topics");
		topicArray.add("channel-bitsevents." + channel.getId());

		logger.debug("Sending Subscription to PubSub: " + objectNode.toString());
		webSocket.sendText(objectNode.toString());
	}

	/**
	 * Method: Check PubSub Socket Status
	 */
	public Boolean checkEndpointStatus() {
		// WebSocket needs to be open
		if(getWebSocket() == null || !getWebSocket().getState().equals(WebSocketState.OPEN)) {
			return false;
		}

		// TODO

		return true;
	}
}
