package me.philippheuer.twitch4j.tmi.chat;

import lombok.Getter;
import lombok.Setter;

import com.jcabi.log.Logger;
import com.neovisionaries.ws.client.*;

import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class WsIrcClient {

	private TwitchClient twitchClient;
	private WebSocket webSocket;

	// Create a token bucket with a capacity of 1 token that refills at a fixed interval of 1 token / 2 sec.
	// [If you send more than 20 commands or messages to the server within 30 seconds, you will be locked out for 30 minutes.]
	private final TokenBucket messageBucket = TokenBuckets.builder()
			.withCapacity(20)
			.withFixedIntervalRefillStrategy(1, 1500, TimeUnit.MILLISECONDS)
			.build();

	// Token bucket for moderated channels by bot
	private final Map<String, TokenBucket> modMessageBucket = new HashMap<String, TokenBucket>();

	public WsIrcClient(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);
	}

	private boolean connect() {
		Logger.info(this, "Connecting to Twitch IRC (WebSocket) [%s]", getTwitchClient().getTwitchIrcEndpoint());

		if(getWebSocket() != null) {
			Logger.debug(this, "Shutting down old Twitch IRC (WebSocket) instance. (reconnect)");
			getWebSocket().disconnect();
		}

		Optional<OAuthCredential> twitchCredential = getTwitchClient().getCredentialManager().getTwitchCredentialsForIRC();

		if (!twitchCredential.isPresent()) {
			Logger.error(this, "The Twitch IRC (WebSocket) Client needs valid Credentials from the CredentialManager.");
			return false;
		}

		try {
			setWebSocket(new WebSocketFactory().createSocket(getTwitchClient().getTwitchIrcEndpoint()));
			getWebSocket().addListener(new WsIrcListener(getTwitchClient()));
			getWebSocket().connect();
			return true;
		} catch (Exception ex) {
			Logger.error(this, "Connection to Twitch IRC (WebSocket) failed: [%s]", ex.getMessage());
			return false;
		}
	}

	private void disconnect() {
		getWebSocket().disconnect();
	}

	private boolean reconnect() {
		disconnect();
		return connect();
	}
}
