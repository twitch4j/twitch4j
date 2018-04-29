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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
	 * Web Socket Lock
	 */
	private ReentrantLock wsLock = new ReentrantLock();

	/**
	 * Twitch Client
	 */
	@Setter(AccessLevel.NONE)
	private final TwitchClient twitchClient;

	/**
	 * List of Joined channels
	 */
	private final Map<String, ChannelCache> channelCache = new HashMap<>();

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
	private final Map<String, TokenBucket> modMessageBucket = new HashMap<>();

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
	}

	/**
	 * Recreate the WebSocket
	 */
	private void createWebSocket() {
		try {
			// WebSocket
			this.ws = new WebSocketFactory().createSocket(Endpoints.IRC.getURL());

			// WebSocket Listeners
			this.ws.clearListeners();
			this.ws.addListener(new WebSocketAdapter() {

				@Override
				public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
					Logger.info(this, "Connecting to Twitch IRC [%s]", Endpoints.IRC.getURL());

					sendCommand("cap req", ":twitch.tv/membership twitch.tv/tags twitch.tv/commands");

					// if credentials is null, it will automatically disconnect
					if (!credential.isPresent()) {
						Logger.error(this, "The Twitch IRC Client needs valid Credentials from the CredentialManager.");
						disconnect();
						return; // do not continue script
					}

					sendCommand("pass", String.format("oauth:%s", credential.get().getToken()));
					sendCommand("nick", credential.get().getUserName());

					// Join defined channels
					if (!getChannelCache().isEmpty()) {
						for (String channel : getChannelCache().keySet()) {
							sendCommand("join", "#" + channel);
						}
					}
					// then join to own channel - required for sending or receiving whispers
					sendCommand("join", "#" + credential.get().getUserName());

					// Connection Success
					setConnectionState(TMIConnectionState.CONNECTED);
				}

				@Override
				public void onTextMessage(WebSocket ws, String text) {
					Arrays.asList(text.replace("\n\r", "\n")
							.replace("\r", "\n").split("\n"))
							.forEach(message -> {
								if (!message.equals("")) {
									// Handle messages
									// - Ping
									if(message.contains("PING :tmi.twitch.tv")) {
										sendPong(":tmi.twitch.tv");
									}
									// - Login failed.
									else if(message.equals(":tmi.twitch.tv NOTICE * :Login authentication failed")) {
										Logger.error(this, "Invalid IRC Credentials. Login failed!");
									}
									// - Parse IRC Message
									else
									{
										try {
											IRCMessageEvent event = new IRCMessageEvent(message);

											if(event.isValid()) {
												getTwitchClient().getDispatcher().dispatch(event);
											} else {
												Logger.trace(this, "Can't parse " + event.getRawMessage());
											}
										} catch (Exception ex) {
											Logger.error(this, ExceptionUtils.getStackTrace(ex));
										}
									}
								}
							});
				}
				public void onDisconnected(WebSocket websocket,
										   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
										   boolean closedByServer) {
					if (!getConnectionState().equals(TMIConnectionState.DISCONNECTING)) {
						Logger.info(this, "Connection to Twitch IRC lost (WebSocket)! Retrying ...");

						// connection lost - reconnecting
						reconnect();
					} else {
						setConnectionState(TMIConnectionState.DISCONNECTED);
						Logger.info(this, "Disconnected from Twitch IRC (WebSocket)!");
					}
				}
			});

		} catch (Exception ex) {
			Logger.error(this, ex.getMessage());
			Logger.trace(this, ExceptionUtils.getStackTrace(ex));
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
		// Lock
		wsLock.lock();

		if (getConnectionState().equals(TMIConnectionState.DISCONNECTED) || getConnectionState().equals(TMIConnectionState.RECONNECTING)) {
			try {
				// Change Connection State
				setConnectionState(TMIConnectionState.CONNECTING);

				// Get Credential from CredentialManager
				credential = twitchClient.getCredentialManager().getTwitchCredentialsForIRC();
				Assert.isTrue(credential.isPresent(), "No valid IRC Credential!");

				// Recreate Socket if state does not equal CREATED
				createWebSocket();

				// Connect to IRC WebSocket
				this.ws.connect();
				wsLock.unlock();

				// Message Bucket
				updateMessageBucket();
			} catch (Exception ex) {
				Logger.error(this, "Connection to Twitch IRC failed: %s - Retrying ...", ex.getMessage());

				try {
					Thread.sleep(1000);
				} catch (Exception et) {
					et.printStackTrace();
				}

				wsLock.unlock();
				reconnect();
			}
		}
	}

	/**
	 * Disconnecting from IRC-WS
	 */
	public void disconnect() {
		wsLock.lock();

		if (getConnectionState().equals(TMIConnectionState.CONNECTED)) {
			setConnectionState(TMIConnectionState.DISCONNECTING);
			sendCommand("QUIT"); // safe disconnect
		}

		setConnectionState(TMIConnectionState.DISCONNECTED);

		// CleanUp
		this.ws.clearListeners();
		this.ws.disconnect();
		this.ws = null;

		wsLock.unlock();
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
		} else {
			Logger.warn(this, String.format("Can't send IRC-WS Command [%s %s]", command.toUpperCase(), String.join(" ", args)));
		}
	}

	/**
	 * Answer to twitch's ping request
	 *
	 * @param arg
	 */
	public void sendPong(String arg) {
		sendCommand("PONG", arg);
	}

	/**
	 * Check if the bot has moderator permissions in a channel.
	 * Uses the channel cache, so it will always return false for channel you didn't join before.
	 *
	 * @param channel Channel.
	 * @return Moderator (true) or User (false)
	 */
	public boolean isModerator(String channel) {
		Optional<User> botUser = getTwitchClient().getUserEndpoint().getUser(credential.get().getUserId());
		// Get Credential from CredentialManager
		if(getChannelCache().containsKey(channel) && botUser.isPresent()) {
			for(User mod : getChannelCache().get(channel).getModerators()) {
				if(mod.getId().equals(botUser.get().getId())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Joining the channel
	 * @param channel channel name
	 */
	public void joinChannel(String channel) {
		if (!channelCache.containsKey(channel)) {
			sendCommand("join", "#" + channel);
			channelCache.put(channel, new ChannelCache(this, channel));

			Logger.debug(this, "Joining Channel [%s].", channel);

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
	 * @deprecated Use {@link #leaveChannel(String)} instead
	 */
	@Deprecated
	public void partChannel(String channelName) {
		leaveChannel(channelName);
	}

	/**
	 * leaving the channel
	 * @param channelName channel name
	 */
	public void leaveChannel(String channelName) {
		Channel channel = twitchClient.getChannelEndpoint(channelName).getChannel();
		if (channelCache.containsKey(channelName)) {
			sendCommand("part", "#" + channel.getName());
			channelCache.remove(channelName);

			Logger.debug(this, "Leaving Channel [%s].", channelName);
		}

		// Remove message bucket
		if (modMessageBucket.containsKey(channelName)) {
			modMessageBucket.remove(channelName);
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
			sendCommand("privmsg", "#" + channel, ":" + message);

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
