package me.philippheuer.twitch4j.message.irc;

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
import me.philippheuer.twitch4j.enums.TMIConnectionState;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.events.event.irc.IrcRawMessageEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

import java.util.*;

@Setter
@Getter
class IRCWebSocket {

	/**
	 * WebSocket Client
	 */
	private WebSocket ws;

	/**
	 * Twitch Client
	 */
	private final TwitchClient twitchClient;

	/**
	 * List of Joined channels
	 */
	private final List<Channel> channels = new ArrayList<Channel>();

	/**
	 * The connection state
	 * Default: ({@link TMIConnectionState#DISCONNECTED})
	 */
	private TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

	/**
	 * IRC WebSocket
	 * @param client TwitchClient.
	 */
	public IRCWebSocket(TwitchClient client) {
		this.twitchClient = client;

		// Create WebSocket
		try {
			this.ws = new WebSocketFactory().createSocket(Endpoints.IRC.getURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// WebSocket Listener
		this.ws.addListener(new WebSocketAdapter() {

			/**
			 * Twitch Client
			 */
			private TwitchClient twitchClient = getTwitchClient();

			@Override
			public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
				setConnectionState(TMIConnectionState.CONNECTING);
				Logger.info(this, "Connecting to Twitch IRC [%s]", Endpoints.IRC.getURL());

				sendCommand("cap req", ":twitch.tv/membership");
				sendCommand("cap req", ":twitch.tv/tags");
				sendCommand("cap req", ":twitch.tv/commands");

				// Find Credential
				OAuthCredential credential = client.getCredentialManager().getTwitchCredentialsForIRC().orElse(null);

				// if credentials is null, it will automatically disconnect
				if (credential == null) {
					Logger.error(this, "The Twitch IRC Client needs valid Credentials from the CredentialManager.");
					setConnectionState(TMIConnectionState.DISCONNECTING); // set state to graceful disconnect (without reconnect looping)
					ws.disconnect();
				}

				sendCommand("pass", String.format("oauth:%s", credential.getToken()));
				sendCommand("nick", credential.getUserName());
				joinChannel(credential.getUserName()); // join to own channel - required for sending or receiving whispers

				// Rejoin Channels on Reconnect
				if (!getChannels().isEmpty()) {
					for (Channel channel : getChannels()) {
						sendCommand("join", "#" + channel.getName());
					}
				}
			}

			@Override
			public void onTextMessage(WebSocket ws, String text) {
				Arrays.asList(text.replace("\n\r", "\n")
						.replace("\r", "\n").split("\n"))
						.forEach(message -> {
					if (!message.equals("")) {
						IRCParser parser = new IRCParser(message);
						Event event = new IrcRawMessageEvent(parser);
						getTwitchClient().getDispatcher().dispatch(event);
					}
				});
			}
			public void onDisconnected(WebSocket websocket,
									   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
									   boolean closedByServer) {
				if (!getConnectionState().equals(TMIConnectionState.DISCONNECTING)) {
					String reason = "Connection to Twitch IRC lost (WebSocket)! Reconnecting...";
					Logger.info(this, reason);

					// connection lost - reconnecting
					setConnectionState(TMIConnectionState.RECONNECTING);
					connect();
				} else {
					setConnectionState(TMIConnectionState.DISCONNECTED);
				}
			}
		});
	}

	/**
	 * Connecting to IRC-WS
	 */
	public void connect() {
		if (getConnectionState().equals(TMIConnectionState.DISCONNECTED) || getConnectionState().equals(TMIConnectionState.RECONNECTING)) {
			try {
				this.ws.connect();
			} catch (Exception ex) {
				Logger.warn(this, "Connection to Twitch IRC failed: %s", ex.getMessage());
			}
		}
	}

	/**
	 * Disconnecting from IRC-WS
	 */
	public void disconnect() {
		if (getConnectionState().equals(TMIConnectionState.CONNECTED)) {
			Logger.info(this, "Disconnecting from Twitch IRC (WebSocket)!");

			setConnectionState(TMIConnectionState.DISCONNECTING);
			sendCommand("QUIT"); // safe disconnect
		}
		this.ws.disconnect();
	}

	/**
	 * Reconnecting to IRC-WS
	 */
	public void reconnect() {
		setConnectionState(TMIConnectionState.RECONNECTING);
		disconnect();
		connect();
	}

	/**
	 * Send IRC Command
	 * @param command IRC Command
	 * @param args command arguments
	 */
	public void sendCommand(String command, String... args) {
		// will send command if connection has been established
		if (getConnectionState().equals(TMIConnectionState.CONNECTED) || getConnectionState().equals(TMIConnectionState.CONNECTING)) {
			// command will be uppercase.
			this.ws.sendText(String.format("%s %s", command.toUpperCase(), String.join(" ", args)));
		}
	}

	/**
	 * Joining the channel
	 * @param channelName channel name
	 */
	public void joinChannel(String channelName) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();

		if (!channels.contains(channel)) {
			sendCommand("join", "#" + channel.getName());
			channels.add(channel);

			Logger.debug(this, "Joining Channel [%s].", channelName);
		}
	}

	/**
	 * leaving the channel
	 * @param channelName channel name
	 */
	public void partChannel(String channelName) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();
		if (channels.contains(channel)) {
			sendCommand("part", "#" + channel.getName());
			channels.remove(channel);

			Logger.debug(this, "Leaving Channel [%s].", channelName);
		}
	}

	/**
	 * Sending message to the joined channel
	 * @param channelName channel name
	 * @param message message
	 */
	public void sendMessage(String channelName, String message) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();
		if (channels.contains(channel)) {
			sendCommand("privmsg", "#" + channel.getName(), message);
		}
	}

	/**
	 * sending private message
	 * @param username username
	 * @param message message
	 */
	public void sendPrivateMessage(String username, String message) {
		Optional<User> twitchUser = twitchClient.getUserEndpoint().getUserByUserName(username);
		OAuthCredential credential = twitchClient.getCredentialManager().getTwitchCredentialsForIRC().orElse(null);
		twitchUser.ifPresent(user -> sendCommand("privmsg", "#" + credential.getUserName(), "/w", user.getName(), message));
	}
}
