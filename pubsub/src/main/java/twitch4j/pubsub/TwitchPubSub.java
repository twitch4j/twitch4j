package twitch4j.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;
import twitch4j.common.events.EventManager;
import twitch4j.pubsub.event.*;
import twitch4j.pubsub.exceptions.PubSubListeningException;
import twitch4j.pubsub.json.PubSubMessage;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TwitchPubSub extends WebSocketClient {

	private static final Map<String, PubSubTopic> PRE_LISTEN = new ConcurrentHashMap<>(50);
	private static final Map<String, PubSubTopic> PRE_UNLISTEN = new ConcurrentHashMap<>();

	private final Set<PubSubTopic> listeners = new LinkedHashSet<>(50);

	private final EventManager eventManager;
	private final ObjectMapper mapper;

	private final Timer timer = new Timer();

	public TwitchPubSub(EventManager eventManager, ObjectMapper mapper) {
		super(URI.create("wss://pubsub-edge.twitch.tv:443"), new Draft_6455());
		this.eventManager = eventManager;
		this.mapper = mapper;
	}

	public boolean ready() {
		return !PRE_LISTEN.isEmpty();
	}

	@Override
	public void connect() {
		if (ready()) {
			super.connect();
		}
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		if (PRE_LISTEN.size() > 0) {
			listen();
			keepAlive();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(String message) {
		try {
			Map<String, String> response = mapper.readValue(message, new TypeReference<Map<String, String>>() {});
			if (response.containsKey("type")) {
				switch (response.get("type")) {
					case "RECONNECT":
						log.warn("Reconnect Required! Reconnecting...");
						listeners.forEach(listener -> {
							if (!PRE_LISTEN.values().contains(listener)) {
								PRE_LISTEN.put(UUID.randomUUID().toString(), listener);
							}
						});
						Reconnect reconnect = new Reconnect();
						reconnect.setPubSub(this);
						eventManager.dispatch(reconnect);
						reconnect();
						break;
					case "RESPONSE":
						PubSubTopic topic = PRE_LISTEN.get(response.get("nonce"));
						if (!response.get("error").equals("")) {
							throw new PubSubListeningException(response.get("error"), topic);
						} else {
							PRE_LISTEN.remove(response.get("nonce"));
							listeners.add(topic);
							TopicListen topicListen = new TopicListen();
							topicListen.setPubSub(this);
							topicListen.setTopic(topic);

							eventManager.dispatch(topicListen);
						}
						break;
					case "MESSAGE":
						PubSubMessage pubSubMessage = mapper.convertValue(response.get("data"), PubSubMessage.class);
						String[] topicMessage = pubSubMessage.getTopic().split(".");
						switch (topicMessage[0]) {
							case "channel-subscribe-events-v1":
								// TODO SubscriptionEvent
								break;
							case "whispers":
								// TODO WhisperEvent
								break;
							case "chat_moderator_actions":
								// TODO ModerationAction https://github.com/TwitchLib/TwitchLib.PubSub/blob/1e13ebee9a9d540e88aa7ca40b9b0e23d37a0ce8/TwitchLib.PubSub/TwitchPubSub.cs#L168-L219
								break;
							case "channel-bits-events-v1":
								// TODO BitsEvent
								break;
							case "channel-commerce-events-v1":
								// TODO CommerceEvent
								break;
							case "channel-ext-v1":
								// TODO ExtensionBroadcastSystemEvent
								break;
							case "video-playback":
								// TODO VideoPlaybackEvent https://github.com/TwitchLib/TwitchLib.PubSub/blob/1e13ebee9a9d540e88aa7ca40b9b0e23d37a0ce8/TwitchLib.PubSub/TwitchPubSub.cs#L260-L271
								break;
							case "following":
								// TODO FollowEvent
								break;
						}
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void keepAlive() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sendPing();
			}
		}, Duration.ofMinutes(5).toMillis(), 250);
	}

	@Override
	public void sendPing() throws NotYetConnectedException {
		send("{\"type\": \"PING\"}");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		log.info("Disconnected{}: [{}] {}", (remote) ? " via remote" : "", code, reason);
		if (code > CloseFrame.NORMAL) {
			listeners.forEach(listener -> {
				if (!PRE_LISTEN.values().contains(listener)) {
					PRE_LISTEN.put(UUID.randomUUID().toString(), listener);
				}
			});
		}
		timer.cancel();
		timer.purge();
	}

	@Override
	public void onError(Exception ex) {
		log.error(ex.getMessage(), ex);
	}

	public void register(PubSubTopic listener) {
		PRE_LISTEN.put(UUID.randomUUID().toString(), listener);
		if (isOpen()) {
			listen();
		}
	}

	public void unregister(PubSubType type, String... subject) {
		listeners.stream().filter(listener -> listener.getTopics().contains(type.subject(subject)))
				.findFirst().ifPresent(listener -> PRE_UNLISTEN.put(UUID.randomUUID().toString(), listener));
		if (isOpen()) {
			unListen();
		}
	}

	private void listen() {
		PRE_LISTEN.forEach((nonce, listener) -> {
			Map<String, Object> listen = new HashMap<>();
			listen.put("type", "LISTEN");
			listen.put("nonce", nonce);
			listen.put("data", listener);

			try {
				send(mapper.writeValueAsBytes(listen));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}

	private void unListen() {
		PRE_UNLISTEN.forEach((nonce, listener) -> {
			Map<String, Object> unlisten = new HashMap<>();
			unlisten.put("type", "UNLISTEN");
			unlisten.put("nonce", nonce);
			unlisten.put("data", listener);

			try {
				send(mapper.writeValueAsBytes(unlisten));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}
}
