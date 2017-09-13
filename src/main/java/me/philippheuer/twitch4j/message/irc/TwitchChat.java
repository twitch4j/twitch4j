package me.philippheuer.twitch4j.message.irc;

import com.jcabi.log.Logger;
import com.neovisionaries.ws.client.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.enums.TMIConnectionState;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.events.event.irc.IRCMessageEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;
import me.philippheuer.twitch4j.model.UserChat;
import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
public class TwitchChat {

	/**
	 * Color Enum
	 */
	public enum Color {
		Blue,
		BlueViolet,
		CadetBlue,
		Chocolate,
		Coral,
		DodgerBlue,
		Firebrick,
		GoldenRod,
		Green,
		HotPink,
		OrangeRed,
		Red,
		SeaGreen,
		SpringGreen,
		YellowGreen
	}

	/**
	 * WebSocket Client
	 */
	@Setter(AccessLevel.NONE)
	private WebSocket ws;

	/**
	 * Twitch Client
	 */
	@Setter(AccessLevel.NONE)
	private final TwitchClient twitchClient;

	/**
	 * List of Joined channels
	 */
	private final Map<String, ChannelCache> channels = new HashMap<String, ChannelCache>();

	/**
	 * The connection state
	 * Default: ({@link TMIConnectionState#DISCONNECTED})
	 */
	private TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

	/**
	 * Token Bucket for message limits
	 */
	private TokenBucket messageBucket;

	/**
	 * Token Bucket for moderated channels
	 */
	// TODO: Moderator Message Bucket moved to channel cache
	private final Map<String, TokenBucket> modMessageBucket = new HashMap<String, TokenBucket>();

	@Getter(AccessLevel.NONE)
	private Optional<OAuthCredential> credential = Optional.empty();

	/**
	 * IRC WebSocket
	 * @param client TwitchClient.
	 */
	public TwitchChat(TwitchClient client) {
		this.twitchClient = client;

		// Create WebSocket
		createWebSocket();

		// WebSocket Listener
		this.ws.addListener(new WebSocketAdapter() {

			@Override
			public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
				setConnectionState(TMIConnectionState.CONNECTING);
				Logger.info(this, "Connecting to Twitch IRC [%s]", Endpoints.IRC.getURL());

				sendCommand("cap req", ":twitch.tv/membership twitch.tv/tags twitch.tv/commands");

				// if credentials is null, it will automatically disconnect
				if (!credential.isPresent()) {
					Logger.error(this, "The Twitch IRC Client needs valid Credentials from the CredentialManager.");
					setConnectionState(TMIConnectionState.DISCONNECTING); // set state to graceful disconnect (without reconnect looping)
					ws.disconnect();
					return; // do not continue script
				}

				sendCommand("pass", String.format("oauth:%s", credential.get().getToken()));
				sendCommand("nick", credential.get().getUserName());

				// Join defined channels
				if (!getChannels().isEmpty()) {
					for (String channel : getChannels().keySet()) {
						sendCommand("join", "#" + channel);
					}
				}
				// then join to own channel - required for sending or receiving whispers
				joinChannel(credential.get().getUserName());
			}

			@Override
			public void onTextMessage(WebSocket ws, String text) {
				Arrays.asList(text.replace("\n\r", "\n")
						.replace("\r", "\n").split("\n"))
						.forEach(message -> {
							if (!message.equals("")) {
								try {
									IRCMessageEvent event = new IRCMessageEvent(message);

									if(event.isValid()) {
										getTwitchClient().getDispatcher().dispatch(event);
									} else {
										// Logger.warn(this, "Can't parse " + event.getRawMessage());
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						});
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

	/**
	 * Recreate the WebSocket
	 */
	private void createWebSocket() {
		try {
			this.ws = new WebSocketFactory().createSocket(Endpoints.IRC.getURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Increased Token Bucket, when <b>isKnownBot</b> is <b>true</b>
	 * @return increased token bucket
	 */
	private TokenBucket setIncreasedMessageBucket() {
		return TokenBuckets.builder()
				.withCapacity(50)
				.withFixedIntervalRefillStrategy(1, 600, TimeUnit.MILLISECONDS)
				.build();
	}

	/**
	 * Default Token Bucket, when <b>isKnownBot</b> is <b>false</b>
	 * @return default token bucket
	 */
	private TokenBucket setDefaultMessageBucket() {
		return TokenBuckets.builder()
				.withCapacity(20)
				.withFixedIntervalRefillStrategy(1, 1500, TimeUnit.MILLISECONDS)
				.build();
	}

	/**
	 * Connecting to IRC-WS
	 */
	public void connect() {
		if (getConnectionState().equals(TMIConnectionState.DISCONNECTED) || getConnectionState().equals(TMIConnectionState.RECONNECTING)) {
			try {
				// Get Credential from CredentialManager
				this.credential = twitchClient.getCredentialManager().getTwitchCredentialsForIRC();
				Assert.isTrue(credential.isPresent(), "No valid IRC Credential!");

				// Recreate Socket if state does not equal CREATED
				if(!ws.getState().equals(WebSocketState.CREATED)) {
					createWebSocket();
				}

				// Connect to IRC WebSocket
				this.ws.connect();
			} catch (Exception ex) {
				Logger.error(this, "Connection to Twitch IRC failed: %s", ex.getMessage());
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
	 * Update the MessageBucket to the correct limits
	 * TODO: Add Mod Check from channel cache here
	 */
	public void updateMessageBucket() {
		if (credential.isPresent()) {
			Optional<UserChat> userChat = twitchClient.getUserEndpoint().getUserChat(credential.get().getUserId());
			messageBucket = (userChat.isPresent() && userChat.get().getIsKnownBot()) ? setIncreasedMessageBucket() : setDefaultMessageBucket();
		} else {
			messageBucket = setDefaultMessageBucket();
		}
	}

	/**
	 * Send IRC Command
	 * @param command IRC Command
	 * @param args command arguments
	 */
	private void sendCommand(String command, String... args) {
		// will send command if connection has been established
		if (getConnectionState().equals(TMIConnectionState.CONNECTED) || getConnectionState().equals(TMIConnectionState.CONNECTING)) {
			// command will be uppercase.
			this.ws.sendText(String.format("%s %s", command.toUpperCase(), String.join(" ", args)));
		}
	}

	public void sendPong(String arg) {
		sendCommand("PONG", arg);
	}

	/**
	 * Check if the user has moderator permissions in a channel.
	 *
	 * @param channel Channel.
	 * @return Boolean
	 */
	public boolean isModerator(String channel) {
		// No Credentials for Channel
		return false;

		/* TODO: Get moderator status for channel cache
		return modMessageBucket.containsKey(channel) || getTwitchClient()
				.getTMIEndpoint()
				.getChatters(channel)
				.getModerators()
				.contains(getCredential().getUserName());
				*/
	}

	/**
	 * Joining the channel
	 * @param channel channel name
	 */
	public void joinChannel(String channel) {
		if (!channels.containsKey(channel)) {
			sendCommand("join", "#" + channel);
			channels.put(channel, new ChannelCache(this, channel));

			Logger.debug(this, "Joining Channel [%s].", channel);
		}

		if (isModerator(channel)) {
			TokenBucket modBucket = TokenBuckets.builder()
					.withCapacity(100)
					.withFixedIntervalRefillStrategy(1, 300, TimeUnit.MILLISECONDS)
					.build();
			modMessageBucket.put(channel, modBucket);
		}
	}

	/**
	 * leaving the channel
	 * @param channelName channel name
	 */
	public void partChannel(String channelName) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();
		if (channels.containsKey(channel)) {
			sendCommand("part", "#" + channel.getName());
			channels.remove(channel);

			Logger.debug(this, "Leaving Channel [%s].", channelName);
		}

		// Remove message bucket
		if (modMessageBucket.containsKey(channel)) {
			modMessageBucket.remove(channel);
		}
	}

	/**
	 * Sending message to the joined channel
	 * @param channel channel name
	 * @param message message
	 */
	public void sendMessage(String channel, String message) {
		Logger.debug(this, "Sending message to channel [%s] with content [%s].!", channel, message);
		new Thread(() -> {
			// Consume 1 Token (wait's in case the limit has been exceeded)
			if (isModerator(channel)) {
				// Only for channels if bot is moderator
				modMessageBucket.get(channel).consume(1);
			} else {
				messageBucket.consume(1);
			}

			// Send Message
			if (channels.containsKey(channel)) {
				sendCommand("privmsg", "#" + channel, message);
			}

			// Logging
			Logger.debug(this, "Message send to Channel [%s] with content [%s].", channel, message);
		}).start();
	}

	public void setColor(String hexColor) {
		// TODO: Assert for Twitch Prime or Turbo subscriptions - don't continue if conditions dose not met
		if (hexColor.matches("[#]?[0-9a-fA-F]{6}$")) {
			if (!hexColor.startsWith("#")) hexColor = "#" + hexColor;
			sendMessage(credential.get().getUserName(), "/color " + hexColor);
		}
	}

	public void setColor(Color color) {
		sendMessage(credential.get().getUserName(), "/color " + color.name());
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

	/**
	 * Gets the status of the IRC Client.
	 *
	 * @return Whether the service or operating normally or not.
	 */
	public Map.Entry<Boolean, String> checkEndpointStatus() {
		// Check
		OAuthCredential credential = getTwitchClient().getCredentialManager().getTwitchCredentialsForIRC().orElse(null);
		if(credential == null) {
			return new AbstractMap.SimpleEntry<>(false, "Twitch IRC Credentials not present!");
		} else {
			if(!credential.getOAuthScopes().contains(TwitchScopes.CHAT_LOGIN.getKey())) {
				return new AbstractMap.SimpleEntry<>(false, "Twitch IRC Credentials are missing the CHAT_LOGIN Scope! Please fix the permissions in your oauth request!");
			}
		}

		return new AbstractMap.SimpleEntry<>(true, "");
	}
}
