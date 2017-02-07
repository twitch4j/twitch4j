package me.philippheuer.twitch4j.chat;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import me.philippheuer.twitch4j.auth.model.twitch.TwitchScopes;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.exception.KittehConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.twitch.TwitchCredential;

@Getter
@Setter
public class IrcClient {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(IrcClient.class);

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
		getLogger().info(String.format("Connecting to Twitch IRC [%s]", getClient().getTwitchIrcEndpoint()));

		// Shutdown, if the client is still running
		if(this.getIrcClient() != null) {
			getLogger().debug(String.format("Shutting down old Twitch IRC instance."));
			getIrcClient().shutdown();
		}

		// Get Credentials
		Optional<TwitchCredential> twitchCredential = getClient().getCredentialManager().getTwitchCredentialsForIRC();

		// Check
		if(!twitchCredential.isPresent()) {
			getLogger().warn("IRC needs valid Credentials from the CredentialManager.");
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
        		.nick(twitchCredential.get().getUser().getName())
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
					} else {
						if(ex instanceof KittehConnectionException) {
							getLogger().warn(String.format("Connection to Twitch IRC [%s] lost.", getClient().getTwitchIrcEndpoint()));
							reconnect();
						}

						ex.printStackTrace();
					}

				}

        	});

        	getLogger().info(String.format("Connected to Twitch IRC [%s]", getClient().getTwitchIrcEndpoint()));

        	return true;
        } catch (Exception ex) {
        	getLogger().error(String.format("Connection to Twitch IRC [%s] Failed: %s", getClient().getTwitchIrcEndpoint(), ex.getMessage()));
            return false;
        }
	}

	/**
	 * Reconnects only if the connection was lost.
	 */
	private void reconnect() {
		getLogger().info(String.format("Reconnecting to Twitch IRC [%s] ...", getClient().getTwitchIrcEndpoint()));

		disconnect();
		connect();
	}

	private void disconnect() {
		getIrcClient().shutdown();
	}

	public void joinChannel(String channelName) {
		String ircChannel = String.format("#%s", channelName);
		if(!getIrcClient().getChannels().contains(ircChannel)) {
			getIrcClient().addChannel(ircChannel);
			getLogger().info(String.format("Joined Channel %s using Twitch IRC [%s] ...", ircChannel, getClient().getTwitchIrcEndpoint()));
		}
	}
	/**
	 * Method: Check IRC Client Status
	 */
	public Map.Entry<Boolean, String> checkEndpointStatus() {
		// Get Credentials
		Optional<TwitchCredential> twitchCredential = getClient().getCredentialManager().getTwitchCredentialsForIRC();

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
