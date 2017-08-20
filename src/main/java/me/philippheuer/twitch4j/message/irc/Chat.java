package me.philippheuer.twitch4j.message.irc;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Chat extends IRCWebSocket {

	private final TokenBucket messageBucket = TokenBuckets.builder()
			.withCapacity(20)
			.withFixedIntervalRefillStrategy(1, 1500, TimeUnit.MILLISECONDS)
			.build();

	private final Map<String, TokenBucket> modMessageBucket = new HashMap<String, TokenBucket>();

	public Chat(TwitchClient client) {
		super(client);
	}

	public boolean isModerator(String channel) {
		return getTwitchClient()
				.getTMIEndpoint()
				.getChatters(channel)
				.getModerators()
				.contains(getCredential().getUserName()) || modMessageBucket.containsKey(channel);
	}

	@Override
	public void joinChannel(String channel) {
		super.joinChannel(channel);
		if (isModerator(channel)){
			TokenBucket modBucket = TokenBuckets.builder()
					.withCapacity(100)
					.withFixedIntervalRefillStrategy(1, 300, TimeUnit.MILLISECONDS)
					.build();
			modMessageBucket.put(channel, modBucket);
		}
	}

	@Override
	public void partChannel(String channel) {
		super.partChannel(channel);
		if (modMessageBucket.containsKey(channel)) modMessageBucket.remove(channel);
	}

	@Override
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
			super.sendMessage(channel, message);

			// Logging
			Logger.debug(this, "Message send to Channel [%s] with content [%s].", channel, message);
		}).start();
	}

	/**
	 * Gets the status of the IRC Client.
	 *
	 * @return Whether the service or operating normally or not.
	 */
	public Map.Entry<Boolean, String> checkEndpointStatus() {
		// Check
		if(getCredential() == null) {
			return new AbstractMap.SimpleEntry<Boolean, String>(false, "Twitch IRC Credentials not present!");
		} else {
			if(!getCredential().getOAuthScopes().contains(TwitchScopes.CHAT_LOGIN.getKey())) {
				return new AbstractMap.SimpleEntry<Boolean, String>(false, "Twitch IRC Credentials are missing the CHAT_LOGIN Scope! Please fix the permissions in your oauth request!");
			}
		}

		return new AbstractMap.SimpleEntry<Boolean, String>(true, "");
	}

}
