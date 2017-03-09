package me.philippheuer.twitch4j.chat;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.model.Channel;
import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.exception.KittehConnectionException;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;

@Getter
@Setter
public class IrcClient {

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * IRC Client Library
	 */
	private Client ircClient;

	// Create a token bucket with a capacity of 1 token that refills at a fixed interval of 1 token / 2 sec.
	// [If you send more than 20 commands or messages to the server within 30 seconds, you will be locked out for 30 minutes.]
	private final TokenBucket messageBucket = TokenBuckets.builder()
			.withCapacity(20)
			.withFixedIntervalRefillStrategy(1, 1500, TimeUnit.MILLISECONDS)
			.build();

	/**
	 * Class Constructor
	 * <p>
	 * This is the irc client wrapper, which allows to easily replace the irc client library.
	 *
	 * @param twitchClient The TwitchClient.
	 */
	public IrcClient(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);

		connect();
	}

	private Boolean connect() {
		Logger.info(this, "Connecting to Twitch IRC [%s]", getTwitchClient().getTwitchIrcEndpoint());

		// Shutdown, if the client is still running
		if(getIrcClient() != null) {
			Logger.debug(this, "Shutting down old Twitch IRC instance. (reconnect)");
			getIrcClient().shutdown();
		}

		// Get Credentials
		Optional<OAuthCredential> twitchCredential = getTwitchClient().getCredentialManager().getTwitchCredentialsForIRC();

		// Check
		if(!twitchCredential.isPresent()) {
			Logger.error(this, "The Twitch IRC Client needs valid Credentials from the CredentialManager.");
			return false;
		}

        try {
        	URI uri = new URI("irc://" + getTwitchClient().getTwitchIrcEndpoint()); // may throw URISyntaxException
        	String host = uri.getHost();
        	Integer port = uri.getPort();

        	if (uri.getHost() == null || uri.getPort() == -1) {
        		throw new URISyntaxException(uri.toString(), "URI must have host and port parts");
        	}

        	setIrcClient(Client.builder()
        		.serverHost(host)
        		.serverPort(port)
        		.serverPassword("oauth:"+twitchCredential.get().getToken())
        		.nick(twitchCredential.get().getUserName())
        		.build());
        	getIrcClient().getEventManager().registerEventListener(new IrcEventHandler(getTwitchClient()));

        	// Request Capabilities
        	getIrcClient().sendRawLine("CAP REQ :twitch.tv/tags");
        	getIrcClient().sendRawLine("CAP REQ :twitch.tv/membership"); // NAMES, JOIN, PART, MODE
        	getIrcClient().sendRawLine("CAP REQ :twitch.tv/commands");

        	// Exception Handling
        	getIrcClient().setExceptionListener(new Consumer<Exception>() {

				@Override
				public void accept(Exception ex) {
					// Filter Exceptions
					if(ex.getMessage().length() > 0 && ex.getMessage().contains("Server version missing")) {
						// Suppress Server version missing exception for twitch compability.
						return;
					} else {
						if(ex instanceof KittehConnectionException) {
							Logger.warn(this, "Connection to Twitch IRC lost. [%s]", getTwitchClient().getTwitchIrcEndpoint());
							reconnect();
							return;
						}

						ex.printStackTrace();
					}
				}
        	});

			Logger.info(this, "Connected to Twitch IRC! [%s]", getTwitchClient().getTwitchIrcEndpoint());

        	return true;
        } catch (Exception ex) {
			Logger.warn(this, "Connection to Twitch IRC failed: %s", ex.getMessage());
            return false;
        }
	}

	/**
	 * Reconnects only if the connection was lost.
	 */
	private void reconnect() {
		disconnect();
		connect();
	}

	private void disconnect() {
		Logger.info(this, "Disconnecting from Twitch IRC!");
		getIrcClient().shutdown();
	}

	/**
	 * Join's a channel, required to listen for messages
	 * @param channelName The channel to join.
	 */
	public void joinChannel(String channelName) {
		if(getIrcClient() == null) {
			Logger.warn(this, "IRC Client not initalized. Can't join [%s]!", channelName);
		}

		String ircChannel = String.format("#%s", channelName);
		if(!getIrcClient().getChannels().contains(ircChannel)) {
			getIrcClient().addChannel(ircChannel);

			Logger.info(this, "Joined Channel [%s]!", channelName);
		}
	}

	/**
	 * Sends a message to a channel.
	 *
	 * @param channelName Channel, the message is send to.
	 * @param message The message to send.
	 */
	public void sendMessage(final String channelName, final String message) {
		new Thread(() -> {
			// Consume 1 Token (wait's in case the limit has been exceeded)
			getMessageBucket().consume(1);

			// Send Message
			getIrcClient().sendMessage("#" + channelName, message);

			// Logging
			Logger.debug(this, "Message send to Channel [%s] with content [%s].", channelName, message);
		}).start();
	}

	/**
	 * Sends a private message to a user.
	 *
	 * @param userName The user that should receive your private message.
	 * @param message The message to send.
	 */
	public void sendPrivateMessage(final String userName, final String message) {
		new Thread(() -> {
			// Consume 1 Token (wait's in case the limit has been exceeded)
			getMessageBucket().consume(1);

			// Send Private Message [Needs a target channel, but the channel itself doesn't matter - so we use the recipients channel]
			getIrcClient().sendMessage("#" + userName, String.format("/w %s %s", userName, message));

			// Logging
			Logger.debug(this, "Private Message send to [%s] with content [%s].", userName, message);
		}).start();
	}

	/**
	 * Purges all chat messages for a user.
	 *
	 * @param channelName Name of the channel.
	 * @param userName Name of the user.
	 */
	public void purgeChatOfUser(final String channelName, final String userName) {
		// Send Message
		sendMessage(channelName, String.format(".timeout %s 1", userName));
	}

	/**
	 * Gets the status of the IRC Client.
	 *
	 * @return Whether the service or operating normally or not.
	 */
	public Map.Entry<Boolean, String> checkEndpointStatus() {
		// Get Credentials
		Optional<OAuthCredential> twitchCredential = getTwitchClient().getCredentialManager().getTwitchCredentialsForIRC();

		// Check
		if(!twitchCredential.isPresent()) {
			return new AbstractMap.SimpleEntry<Boolean, String>(false, "Twitch IRC Credentials not present!");
		} else {
			if(!twitchCredential.get().getOAuthScopes().contains(TwitchScopes.CHAT_LOGIN.getKey())) {
				return new AbstractMap.SimpleEntry<Boolean, String>(false, "Twitch IRC Credentials are missing the CHAT_LOGIN Scope! Please fix the permissions in your oauth request!");
			}
		}

		return new AbstractMap.SimpleEntry<Boolean, String>(true, "");
	}
}
