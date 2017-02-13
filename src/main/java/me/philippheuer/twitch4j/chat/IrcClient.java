package me.philippheuer.twitch4j.chat;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.model.Channel;
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
	private TwitchClient client;

	/**
	 * IRC Client Library
	 */
	private Client ircClient;

	/**
	 * Constructor
	 */
	public IrcClient(TwitchClient client) {
		setClient(client);

		connect();
	}

	private Boolean connect() {
		Logger.info(this, "Connecting to Twitch IRC [%s]", getClient().getTwitchIrcEndpoint());

		// Shutdown, if the client is still running
		if(getIrcClient() != null) {
			Logger.debug(this, "Shutting down old Twitch IRC instance. (reconnect)");
			getIrcClient().shutdown();
		}

		// Get Credentials
		Optional<OAuthCredential> twitchCredential = getClient().getCredentialManager().getTwitchCredentialsForIRC();

		// Check
		if(!twitchCredential.isPresent()) {
			Logger.error(this, "The Twitch IRC Client needs valid Credentials from the CredentialManager.");
			return false;
		}

        try {
        	URI uri = new URI("irc://" + getClient().getTwitchIrcEndpoint()); // may throw URISyntaxException
        	String host = uri.getHost();
        	Integer port = uri.getPort();

        	if (uri.getHost() == null || uri.getPort() == -1) {
        		throw new URISyntaxException(uri.toString(), "URI must have host and port parts");
        	}

        	setIrcClient(Client.builder()
        		.serverHost(host)
        		.serverPort(port)
        		.serverPassword("oauth:"+twitchCredential.get().getOAuthToken())
        		.nick(twitchCredential.get().getUserName())
        		.build());
        	getIrcClient().getEventManager().registerEventListener(new IrcEventHandler(getClient(), this));

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
							Logger.warn(this, "Connection to Twitch IRC lost. [%s]", getClient().getTwitchIrcEndpoint());
							reconnect();
							return;
						}

						ex.printStackTrace();
					}
				}
        	});

			Logger.info(this, "Connected to Twitch IRC! [%s]", getClient().getTwitchIrcEndpoint());

        	return true;
        } catch (Exception ex) {
			Logger.warn(this, "Connection to Twitch IRC failed: %s",  getClient().getTwitchIrcEndpoint(), ex.getMessage());
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
		String ircChannel = String.format("#%s", channelName);
		if(!getIrcClient().getChannels().contains(ircChannel)) {
			getIrcClient().addChannel(ircChannel);

			Logger.info(this, "Joined Channel [%s]!", channelName);
		}
	}

	/**
	 * Send message to channel
	 * @param channelName Channel, the message is send to.
	 * @param message The message to send.
	 */
	public void sendMessage(String channelName, String message) {
		getIrcClient().sendMessage("#" + channelName, message);
	}

	/**
	 * Method: Check IRC Client Status
	 */
	public Map.Entry<Boolean, String> checkEndpointStatus() {
		// Get Credentials
		Optional<OAuthCredential> twitchCredential = getClient().getCredentialManager().getTwitchCredentialsForIRC();

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
