package twitch4j.irc;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.philippheuer.events4j.EventManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import reactor.core.publisher.Mono;
import twitch4j.Configuration;
import twitch4j.irc.chat.ChannelCache;
import twitch4j.irc.chat.IChannel;
import twitch4j.irc.chat.message.Message;
import twitch4j.irc.chat.message.MessageCommand;
import twitch4j.irc.event.AbstractTMIEvent;
import twitch4j.irc.parse.Parser;

@Slf4j
@Getter
public class TwitchMessageInterface extends WebSocketClient {
	private final Configuration configuration;
	private final ChannelCache cache = new ChannelCache(this);
	private final EventManager eventManager;
	private final Set<Parser<?>> parsers = new LinkedHashSet<>();

	private final MessageInterfaceAPI api;

	@Getter(AccessLevel.NONE)
	private final Set<String> channels = new LinkedHashSet<>();

	public TwitchMessageInterface(Configuration configuration, EventManager eventManager, MessageInterfaceAPI tmiApi) {
		super(URI.create("wss://irc-ws.chat.twitch.tv/"), new Draft_6455());
		this.configuration = configuration;
		this.eventManager = eventManager;
		this.api = tmiApi;
	}

	@Override
	public void sendPing() throws NotYetConnectedException {
		send("PING " + System.currentTimeMillis());
	}


	public void sendPong() throws NotYetConnectedException {
		send("PONG :tmi.twitch.tv");
	}

	public void registerParser(Parser<?> parser) {
		parsers.add(parser);
	}

	public void unregisterParser(Parser<?> parser) {
		parsers.remove(parser);
	}

	public void join(String channel) {
		if (isClosed()) {
			channels.add(channel);
		} else {
			cache.joinChannel(channel);
		}
	}

	public void leave(String channel) {
		if (!isOpen()) {
			channels.remove(channel);
		} else {
			cache.leaveChannel(channel);
		}
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		sendCap().then(sendLogin())
				.doOnSuccess(v -> channels.forEach(cache::joinChannel))
				.block();
	}

	private Mono<Void> sendCap() {
		return Mono.fromCallable(() -> {
			send("CAP REQ :twitch.tv/membership");
			send("CAP REQ :twitch.tv/tags");
			send("CAP REQ :twitch.tv/commands");
			return null;
		});
	}

	private Mono<Void> sendLogin() {
		return Mono.fromCallable(() -> {
			send("PASS oauth:" + configuration.getBotCredentials().accessToken());
			send("NICK " + configuration.getBotCredentials().username());
			// Default join to bot channel to handling private messages
			join(configuration.getBotCredentials().username());
			return null;
		});
	}

	@Override
	public void onMessage(String rawMessage) {
		if (rawMessage.contains(System.lineSeparator())) {
			for (String rawMsg : rawMessage.split(System.lineSeparator())) {
				dispatch(rawMsg);
			}
		} else {
			dispatch(rawMessage);
		}
	}

	@SuppressWarnings("unchecked")
	private void dispatch(String rawMessage) {
		System.out.println(rawMessage);
		Message message = Message.of(rawMessage);
		if (message.getCommand().equals(MessageCommand.PING)) {
			sendPong();
		} else {
			// handle and Dispatch own parsers
			parsers.stream().filter(parser -> parser.parse(message) != null)
					.findFirst().ifPresent(parser -> {
				AbstractTMIEvent event = parser.parse(message);
				event.setMessageInterface(this);
				eventManager.dispatchEvent(event);
			});
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// before purge cache it if you remotely reconnect to latest joined channels
		channels.addAll(cache.getChannels().stream().filter(channel -> !channel.getName().equalsIgnoreCase(configuration.getBotCredentials().username()))
				.map(IChannel::getName).collect(Collectors.toList()));
		cache.purge();
	}

	@Override
	public void onError(Exception ex) {
		log.error(ex.getMessage(), ex);
	}

	public boolean ready() {
		return configuration.getBotCredentials() != null;
	}
}
