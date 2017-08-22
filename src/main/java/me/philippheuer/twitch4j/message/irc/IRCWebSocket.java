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
import me.philippheuer.twitch4j.model.Channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Setter
@Getter
class IRCWebSocket {

	/**
	 * WebSocket Client
	 */
	private WebSocket ws;

	/**
	 * IRC Listeners
	 */
	private final IRCListener listeners;

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
		this.listeners = new IRCListener(client);

		// Create WebSocket
		try {
			this.ws = new WebSocketFactory().createSocket(Endpoints.IRC.getURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// WebSocket Listener
		this.ws.addListener(new WebSocketAdapter() {

			private OAuthCredential credential;

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

				// Rejoin Channels on Reconnect
				if (!getChannels().isEmpty()) {
					for (Channel channel : getChannels()) {
						sendCommand("join", "#" + channel.getName());
					}
				}
			}

			@Override
			public void onTextMessage(WebSocket ws, String text) {
				String[] messages = text
						.replace("\n\r", "\n")
						.replace("\r", "\n").split("\n");
				List<String> msgs = Arrays.asList(messages);
				msgs.removeAll(Arrays.asList(""));
				msgs.toArray(messages);
				for (String message : messages) {
//					final Pattern MESSAGE_REGEX = Pattern.compile("^(?:@(.*?) )?(?::(.+?)(?:!(.+?))?(?:@(.+?))? )?((?:[A-z]+)|(?:[0-9]{3}))(?: (?!:)(.+?))?(?: :(.*))?$");
//					final Matcher matcher = MESSAGE_REGEX.matcher(message);
//					matcher.find();
//					for(int i = 0; i <= matcher.groupCount(); ++i) {
//						if (i == 0) Logger.debug(this,"[IRC-WS] > %s" , matcher.group(i));
//						else System.out.println(matcher.group(i));
//					}
					if (!message.isEmpty() || message != null) {
						IRCParser parser = new IRCParser(getTwitchClient(), message);
						Logger.debug(this, "[IRC-WS] > %s", parser.toString()); // raw message debugging
						if (message.contains("PING")) {
							Logger.debug(this, "Pings received, sending Pong to the Twitch IRC (WebSocket)");
							sendCommand("PONG", ":tmi.twitch.tv");
						} else if (message.contains("PONG")) {
							Logger.debug(this, "Pongs received from Twitch IRC (WebSocket)");
						} else if (message.contains("001")) {
							Logger.info(this, "Connected to Twitch IRC (WebSocket)! [%s]", Endpoints.IRC.getURL());

							setConnectionState(TMIConnectionState.CONNECTED);
						} else {
							listeners.listen(parser);
						}
					} else continue;
				}
			}
			public void onDisconnected(WebSocket websocket,
									   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
									   boolean closedByServer) {
				if (!getConnectionState().equals(TMIConnectionState.DISCONNECTING)) {
					Logger.info(this, "Connection to Twitch IRC lost (WebSocket)! Reconnecting...");

					// connection lost - reconnecting
					setConnectionState(TMIConnectionState.RECONNECTING);
					connect();
				} else {
					setConnectionState(TMIConnectionState.DISCONNECTED);
				}
			}
		});
	}

	public void connect() {
		if (getConnectionState().equals(TMIConnectionState.DISCONNECTED)) {
			try {
				this.ws.connect();
			} catch (Exception ex) {
				Logger.warn(this, "Connection to Twitch IRC failed: %s", ex.getMessage());
			}
		}
	}

	public void disconnect() {
		if (getConnectionState().equals(TMIConnectionState.CONNECTED)) {
			Logger.info(this, "Disconnecting from Twitch IRC (WebSocket)!");

			setConnectionState(TMIConnectionState.DISCONNECTING);
			this.ws.disconnect();
		}
	}

	public void reconnect() {
		disconnect();
		connect();
	}

	public void sendCommand(String command, String... args) {
		// will send command if connection has been established
		if (getConnectionState().equals(TMIConnectionState.CONNECTED) || getConnectionState().equals(TMIConnectionState.CONNECTING)) {
			// command will be uppercase.
			this.ws.sendText(String.format("%s %s", command.toUpperCase(), String.join(" ", args)));
		}
	}

	public void joinChannel(String channelName) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();

		if (!channels.contains(channel)) {
			sendCommand("join", "#" + channel.getName());
			channels.add(channel);

			Logger.debug(this, "Joining Channel [%s].", channelName);
		}
	}

	public void partChannel(String channelName) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();
		if (channels.contains(channel)) {
			sendCommand("part", "#" + channel.getName());
			channels.remove(channel);

			Logger.debug(this, "Leaving Channel [%s].", channelName);
		}
	}

	public void sendMessage(String channelName, String message) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();
		sendCommand("privmsg", "#" + channel.getName(), message);
	}

	public void sendPrivateMessage(String channelName, String message) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();
		sendCommand("privmsg", "#" + channelName, "/w", channel.getName(), message);
	}
}
