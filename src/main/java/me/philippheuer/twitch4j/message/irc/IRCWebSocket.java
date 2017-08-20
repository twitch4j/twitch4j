package me.philippheuer.twitch4j.message.irc;

import com.jcabi.log.Logger;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.CredentialManager;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.enums.TMIConnection;
import me.philippheuer.twitch4j.model.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class IRCWebSocket {

	/**
	 * WebSocket Client
	 */
	private WebSocket ws;
	/**
	 * IRC Listeners
	 */
	private final IRCListener listeners;
	private final TwitchClient twitchClient;

	/**
	 * {@link CredentialManager#getTwitchCredentialsForIRC()}
	 */
	private OAuthCredential credential;

	/**
	 * List of Joined channels
	 */
	private final List<Channel> channels = new ArrayList<Channel>();

	/**
	 * The connection statement
	 * Default: ({@link TMIConnection#DISCONNECTED})
	 */
	private TMIConnection connection = TMIConnection.DISCONNECTED;

	public IRCWebSocket(TwitchClient client) {
		this.twitchClient = client;
		this.listeners = new IRCListener(client);
		try {
			this.ws = new WebSocketFactory().createSocket(Endpoints.IRC.getURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		IRCWebSocket self = this;
		this.ws.addListener(new WebSocketAdapter() {
			@Override
			public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
				setConnection(TMIConnection.CONNECTING);
				Logger.info(this, "Connecting to Twitch IRC [%s]", Endpoints.IRC.getURL());

				sendCommand("cap req", ":twitch.tv/membership");
				sendCommand("cap req", ":twitch.tv/tags");
				sendCommand("cap req", ":twitch.tv/commands");
				// if credentials is null, it will automatically disconnect
				if (getCredential() == null) {
					Logger.error(this, "The Twitch IRC Client needs valid Credentials from the CredentialManager.");
					setConnection(TMIConnection.DISCONNECTING); // set state to graceful disconnect (without reconnect looping)
					ws.disconnect();
				}
				sendCommand("pass", String.format("oauth:%s", getCredential().getToken()));
				sendCommand("nick", getCredential().getUserName());
			}

			@Override
			public void onTextMessage(WebSocket ws, String message) {
				Logger.info(this, message);
				if (message.contains("PING")) {
					// Log ping received message
					sendCommand("PONG", ":tmi.twitch.tv");
				} else if (message.startsWith(":tmi.twitch.tv PONG")) {
					// Log pong received message
				} else if (message.contains(":tmi.twitch.tv 001 " + credential.getUserName() + " :Welcome, GLHF!")) {
					setConnection(TMIConnection.CONNECTED);
					Logger.info(this, "Connected to Twitch IRC (WebSocket)! [%s]", Endpoints.IRC.getURL());
				} else {
					IRCParser parser = new IRCParser(getTwitchClient(), message);
					listeners.listen(parser);
				}
			}
			public void onDisconnected(WebSocket websocket,
									   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
									   boolean closedByServer) {
				if (!getConnection().equals(TMIConnection.DISCONNECTING)) {
					// connection lost - reconnecting
					Logger.info(this, "Connection lost from Twitch IRC (WebSocket)! Reconnecting...");
					setConnection(TMIConnection.RECONNECTING);
					connect();
				} else {
					setConnection(TMIConnection.DISCONNECTED);
				}
			}
		});
		client.getCredentialManager().getTwitchCredentialsForIRC().ifPresent(this::setCredential);

	}

	public void connect() {
		if (connection.equals(TMIConnection.DISCONNECTED)) {
			try {
				this.ws.connect();
			} catch (Exception ex) {
				Logger.warn(this, "Connection to Twitch IRC failed: %s", ex.getMessage());
			}
		}
	}

	public void disconnect() {
		if (connection.equals(TMIConnection.CONNECTED)) {
			Logger.info(this, "Disconnecting from Twitch IRC (WebSocket)!");
			connection = TMIConnection.DISCONNECTING;
			this.ws.disconnect();
		}
	}

	public void reconnect() {
		disconnect();
		connect();
	}

	public void sendCommand(String command, String... args) {
		// command will be uppercase.
		this.ws.sendText(String.format("%s %s", command.toUpperCase(), String.join(" ", args)));
	}

	public void joinChannel(String channel) {
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		if (!channels.contains(ch)) sendCommand("join", "#" + ch.getName());
	}

	public void partChannel(String channel) {
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		if (channels.contains(ch)) sendCommand("part", "#" + ch.getName());
	}
	public void sendMessage(String channel, String message) {
		Channel ch = twitchClient.getChannelEndpoint(channel).getChannel();
		sendCommand("privmsg", "#" + ch.getName(), message);
	}
}
