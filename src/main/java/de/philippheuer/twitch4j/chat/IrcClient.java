package de.philippheuer.twitch4j.chat;

import java.net.URI;
import java.net.URISyntaxException;

import org.kitteh.irc.client.library.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.philippheuer.twitch4j.TwitchClient;
import lombok.*;

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
	private TwitchClient api;
	
	/**
	 * IRC Client Library
	 */
	Client client;
	
	/**
	 * Constructor
	 */
	public IrcClient(TwitchClient api) {
		setApi(api);
		
		connect();
	}
	
	
	private Boolean connect() {
		getLogger().info(String.format("Connecting to Twitch IRC [%s]", getApi().getTwitchIrcEndpoint()));

        try {
        	URI uri = new URI("irc://" + getApi().getTwitchIrcEndpoint()); // may throw URISyntaxException
        	String host = uri.getHost();
        	Integer port = uri.getPort();
        	
        	if (uri.getHost() == null || uri.getPort() == -1) {
        		throw new URISyntaxException(uri.toString(), "URI must have host and port parts");
        	}
        	
        	setClient(Client.builder()
        		.serverHost(host)
        		.serverPort(port)
        		.stsStorageManager(null)
        		.serverPassword("oauth:xxx")
        		.nick("xxx")
        		.build());
        	getClient().getEventManager().registerEventListener(new IrcEventManager(getApi(), this));
        	
        	// Request Capabilties
        	//  Listening to twitch notifications (Subs, ...)
        	getClient().sendRawLine("CAP REQ :twitch.tv/tags");
        	getClient().sendRawLine("CAP REQ :twitch.tv/membership");
        	getClient().sendRawLine("CAP REQ :twitch.tv/commands");
        	
        	getLogger().info(String.format("Connected to Twitch IRC [%s]", getApi().getTwitchIrcEndpoint()));
        	
        	return true;
        } catch (Exception ex) {
        	getLogger().error(String.format("Connection to Twitch IRC [%s] Failed: %s", getApi().getTwitchIrcEndpoint(), ex.getMessage()));
            return false;
        }
	}
	
	
}
