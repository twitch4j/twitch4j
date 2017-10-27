package me.philippheuer.twitch4j.message.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jcabi.log.Logger;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.enums.PubSubTopics;
import me.philippheuer.twitch4j.enums.TMIConnectionState;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.util.conversion.RandomizeString;

import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class TwitchPubSub {

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
	 * The connection state
	 * Default: ({@link TMIConnectionState#DISCONNECTED})
	 */
	private TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

	/**
	 * Random string to identify the response associated with this request. Using {@link RandomizeString} script
	 */
	private final String identifyNonce = new RandomizeString(16).toString();

	/**
	 * Listening channel list
	 */
	private final Map<Channel, List<PubSubTopics>> channelList = new HashMap<Channel, List<PubSubTopics>>();

	/**
	 * Class Constructor
	 *
	 * @param twitchClient TwitchClient is the core class for all api operations.
	 */
	public TwitchPubSub(TwitchClient twitchClient) {
		this.twitchClient = twitchClient;

		// Connect to Twitch PubSub Server
		try {
			webSocket = new WebSocketFactory().createSocket(Endpoints.PUBSUB.getURL());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Register Listener
		webSocket.addListener(new WebSocketAdapter() {
			@Override
			public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
				listenAll();
				setConnectionState(TMIConnectionState.CONNECTED);

				// Schedule Tasks
				scheduleTasks();
			}

			@Override
			public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
				Logger.info(this, "Connection to Twitch PubSub Closed by Server [%s]", Endpoints.PUBSUB.getURL());

				if (!getConnectionState().equals(TMIConnectionState.DISCONNECTING)) {
					reconnect();
				} else {
					setConnectionState(TMIConnectionState.DISCONNECTED);
				}
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
							// TODO: parse Messages (I'm on it [D.S.])
						default:
							return;
					}
				}

				if (jsonNode.has("error") && jsonNode.get("error") != null & !jsonNode.get("error").textValue().isEmpty()) {
					switch (jsonNode.get("error").textValue().toUpperCase()) {
						case "ERR_BADMESSAGE":
							Logger.error(this, "Twitch PubSub encountered an error: %s", "Wrong message format.");
							break;
						case "ERR_BADAUTH":
							Logger.error(this, "Twitch PubSub encountered an error: %s", "Bad OAuth key or OAuth key doesn't have authorized scope to specify topic.");
							break;
						case "ERR_SERVER":
							Logger.error(this, "Twitch PubSub encountered an error: %s", "Server Error.");
							break;
						case "ERR_BADTOPIC":
							Logger.error(this, "Twitch PubSub encountered an error: %s", "Some topics is unknown.");
							break;
						default:
							return;
					}
				}
			}
		});
	}

	/**
	 * Setting topic
	 * @param channel {@link Channel} user model
	 * @param topics list of {@link PubSubTopics}
	 */
	private void setTopic(Channel channel, PubSubTopics... topics) {
		List<PubSubTopics> topicList;
		if (!channelList.containsKey(channel)) topicList = new ArrayList<PubSubTopics>();
		else topicList = channelList.get(channel);
		for (PubSubTopics topic : topics) {
			if (!hasTopic(channel, topic) && !topicList.contains(topic)) {
				topicList.add(topic);
			}
		}
		if (channelList.containsKey(channel)) channelList.replace(channel, topicList);
		else channelList.put(channel, topicList);
	}

	/**
	 * Releasing topic
	 * @param channel {@link Channel} user model
	 * @param topics list of {@link PubSubTopics}
	 */
	private void releaseTopic(Channel channel, PubSubTopics... topics) {
		List<PubSubTopics> topicList;
		if (channelList.containsKey(channel)) topicList = channelList.get(channel);
		else return;
		for (PubSubTopics topic : topics) {
			if (topicList.contains(topic)) topicList.remove(topic);
		}
		if (topicList.isEmpty()) channelList.remove(channel);
		else channelList.replace(channel, topicList);
	}

	/**
	 * Checking topic existence
	 * @param channel {@link Channel} user model
	 * @param topic {@link PubSubTopics} enum
	 * @return topic exists
	 */
	private boolean hasTopic(Channel channel, PubSubTopics topic) {
		return channelList.containsKey(channel) && channelList.get(channel).contains(topic);
	}

	/**
	 * Connecto to PubSub
	 */
	public void connect() {
		if (getConnectionState().equals(TMIConnectionState.DISCONNECTED)) {
			try {
				getWebSocket().connect();
				setConnectionState(TMIConnectionState.CONNECTING);
			} catch (Exception ex) {
				setConnectionState(TMIConnectionState.DISCONNECTED);

				Logger.error(this, "Connection to Twitch PubSub failed: [%s]", ex.getMessage());
			}
		} else {
			Logger.warn(this, "Cannot connecting to Twitch PubSub: is already [%s].", getConnectionState().name().toUpperCase());
		}
	}

	/**
	 * Reconnecting to PubSub
	 */
	public void reconnect() {
		setConnectionState(TMIConnectionState.RECONNECTING);
		disconnect(false);
		scheduleTasks();
		listenAll();
	}

	/**
	 * schedule tasks for pinging WebSocket server.
	 */
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
	 * Disconnect from PubSub
	 * @param forceDisconnect forcing disconnection - if it false will reconnecting automatically
	 */
	public void disconnect(boolean forceDisconnect) {
		// Forced Disconnect?
		if (forceDisconnect) {
			setConnectionState(TMIConnectionState.DISCONNECTING);
		}
		unlistenAll();
		webSocket.disconnect();
		cancelTasks();
	}

	/**
	 * Disconnect from PubSub by force
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
	 * listen all defined channels by {@link TwitchPubSub#listenChannel(Channel, PubSubTopics...)} or {@link TwitchPubSub#listenChannel(Channel, PubSubTopics...)}
	 */
	public void listenAll() {
		if (!channelList.isEmpty() && getConnectionState().equals(TMIConnectionState.CONNECTED))
			channelList.forEach((channel, topics) -> listenChannel(channel, (PubSubTopics[]) topics.toArray()));
	}

	/**
	 * unlisten all defined channels by {@link TwitchPubSub#listenChannel(Channel, PubSubTopics...)} or {@link TwitchPubSub#listenChannel(Channel, PubSubTopics...)}
	 */
	public void unlistenAll() {
		if (!channelList.isEmpty() && getConnectionState().equals(TMIConnectionState.CONNECTED))
			channelList.forEach((channel, topics) -> unlistenChannel(channel, (PubSubTopics[]) topics.toArray()));

	}

	/**
	 * Executing data to Twitch PubSub
	 * @param type data type ("LISTEN" or "UNLISTEN")
	 * @param topics string {@link List} of topics
	 */
	private void execType(String type, List<String> topics) {
		// Check Connection
		if (!getConnectionState().equals(TMIConnectionState.CONNECTED)) {
			return;
		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("type", type.toUpperCase())
				.put("nonce", identifyNonce);
		ArrayNode topicList = mapper.createArrayNode();
		topics.forEach(topicList::add);
		node.putObject("data")
				.put("auth_token", "") // TODO: OAuth if exist.
				.putArray("topics").addAll(topicList);
		Logger.debug(this, "Sending to PubSub - type: %s, topics: [ %s ]", type.toUpperCase(), String.join(", ", topics));
		webSocket.sendText(node.toString());
	}

	/**
	 * Listening channel
	 * @param channel {@link Channel} user model
	 * @param getAll force all {@link PubSubTopics}. To using specified topics use {@link TwitchPubSub#listenChannel(Channel, PubSubTopics...)}
	 */
	public void listenChannel(Channel channel, boolean getAll) {
		List<String> topicList = new ArrayList<String>();
		if (!getAll && channelList.containsKey(channel) && channelList.get(channel).size() > 0)
			listenChannel(channel, (PubSubTopics[]) channelList.get(channel).toArray());
		else listenChannel(channel, PubSubTopics.values());
	}

	/**
	 * Listening specified topics channel
	 * @param channel {@link Channel} user model
	 * @param topics list of {@link PubSubTopics}
	 */
	public void listenChannel(Channel channel, PubSubTopics... topics) {
		List<String> topicList = new ArrayList<String>();
		OAuthCredential channelCredential = null;
		if (channel.getTwitchCredential().isPresent()) {
			channelCredential = channel.getTwitchCredential().get();
		}
		if (channelCredential != null) {
			for (PubSubTopics topic : topics) {
				if (topic.isInRequiredScope((TwitchScopes[]) channelCredential.getOAuthScopes().toArray())) topicList.add(topic.getTopic(channel));
			}
			if (topicList.size() > 0) {
				execType("LISTEN", topicList);
				setTopic(channel, (PubSubTopics[]) topicList.toArray());
			}
		}
	}

	/**
	 * Un-Listening specified topics channel
	 * @param channel {@link Channel} user model
	 * @param topics list of {@link PubSubTopics}
	 */
	public void unlistenChannel(Channel channel, PubSubTopics... topics) {
		List<String> topicList = new ArrayList<String>();
		for (PubSubTopics topic : topics) {
			if (hasTopic(channel, topic)) topicList.add(topic.getTopic(channel));
		}
		if (topicList.size() > 0) {
			execType("UNLISTEN", topicList);
			releaseTopic(channel, (PubSubTopics[]) topicList.toArray());
		}
	}

	/**
	 * Un-Listening channel
	 * @param channel {@link Channel} user model
	 */
	public void unlistenChannel(Channel channel) {
		unlistenChannel(channel, (PubSubTopics[]) channelList.get(channel).toArray());
	}


	/**
	 * Method: Check PubSub Socket Status
	 *
	 * @return True, if the socket is connected. False, if there are problems with the pubsub endpoint.
	 */
	public boolean checkEndpointStatus() {
		return getConnectionState().equals(TMIConnectionState.CONNECTED);
	}
}
