package de.philippheuer.twitch4j.pubsub;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.*;

import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.endpoints.AbstractTwitchEndpoint;
import lombok.*;

@Getter
@Setter
public class TwitchPubSub {
	
	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(AbstractTwitchEndpoint.class);
	
	/**
	 * Holds the API Instance
	 */
	private Twitch4J api;
	
	/**
	 * WebSocketFactory
	 */
	WebSocket webSocket;
	
	/**
	 * Timer for Scheduler Tasks
	 */
	private final Timer timer = new Timer();
	
	/**
	 * Constructor
	 */
	public TwitchPubSub(Twitch4J api) {
		setApi(api);
		
		// Connect to twitch pubsub server
		try {
			setWebSocket(new WebSocketFactory().createSocket(getApi().getTwitchPubSubEndpoint()));
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
	            // Authenticate and Subscribe to Topics
	        	/*
	        	ObjectMapper mapper = new ObjectMapper();
				ObjectNode objectNode = mapper.createObjectNode();
				objectNode.put("type", "LISTEN");
				objectNode.put("nonce", "channelId");
				
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("auth_token", "");
				dataMap.put("topics", {"text"});
				objectNode.put("data", dataMap);
				
				webSocket.sendText(objectNode.toString());
				*/
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
                    
                    // Check for NULL
                    if(jsonNode.get("type") == null) {
                    	ws.disconnect();
                    	return;
                    }
                    
                    // Handle PONG
                    if(jsonNode.get("type").textValue().equalsIgnoreCase("pong")) {
                    	getLogger().debug("Recieved PONG Response from Twitch PubSub.");
                    }
                    
                    // Handle Reconnect
                    if(jsonNode.get("type").textValue().equalsIgnoreCase("reconnect")) {
                    	getLogger().debug("Twitch PubSub is forcing us to reconnect ...");
                    	reconnect();
                    }
                    
                    // Handle Error
                    if(jsonNode.get("error") != null & jsonNode.get("error").textValue().length() > 0) {
                    	getLogger().debug(String.format("Twitch PubSub encountered an error: %s", jsonNode.get("error").textValue()));
                    }
                    
                    // Handle Message
                    if(jsonNode.get("type").textValue().equalsIgnoreCase("message")) {
                    	getLogger().debug(String.format("Recieved a message from Twitch PubSub [%s]", message));
                    	// @TODO: parse Messages
                    }
                    
                } catch (Exception ex) {
                	
                } finally {
                	// Close the WebSocket connection.
                    ws.disconnect();
                } 
            }
            
            /**
             * Event: ConnectionClosed
             */
            @Override
            public void onDisconnected(WebSocket websocket,
                    WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                    boolean closedByServer) {
            	
            }
        });
		
		// Connect
		connect();
	}
	
	private Boolean connect() {
		getLogger().debug(String.format("Connecting to Twitch PubSub [%s]", getApi().getTwitchPubSubEndpoint()));

        try { 
            getWebSocket().connect();
            
            // Schedule Tasks
    		scheduleTasks();
            return true;
        } catch (Exception ex) {
        	getLogger().error(String.format("Connection to Twitch PubSub [%s] Failed: %s", getApi().getTwitchPubSubEndpoint(), ex.getMessage()));
            return false;
        }
	}
	
	private void reconnect() {
		getLogger().debug(String.format("Reconnecting to Twitch PubSub [%s]", getApi().getTwitchPubSubEndpoint()));
		
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
					logger.debug("Send Ping to Twitch PubSub. (Keep-Connection-Alive");
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
	
	
	public void registerTopicListener() {
		getWebSocket().sendText("{'type': 'PING'}");
	}
	
}
