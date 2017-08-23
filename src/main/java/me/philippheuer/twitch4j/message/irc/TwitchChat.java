package me.philippheuer.twitch4j.message.irc;

import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TwitchChat extends IRCWebSocket {

	/**
	 * Token Bucket for message limits
	 */
	private final TokenBucket messageBucket = TokenBuckets.builder()
			.withCapacity(20)
			.withFixedIntervalRefillStrategy(1, 1500, TimeUnit.MILLISECONDS)
			.build();

	/**
	 * Token Bucket for moderated channels
	 */
	private final Map<String, TokenBucket> modMessageBucket = new HashMap<String, TokenBucket>();
	// TODO: Token Bucket for increased rate limits - annotating thread: https://discuss.dev.twitch.tv/t/have-a-chat-whisper-bot-let-us-know/10651

	/**
	 * Twitch Chat IRC Wrapper
	 *
	 * @param client Twitch Client.
	 */
	public TwitchChat(TwitchClient client) {
		super(client);
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

		/* TODO: TEMP
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
	@Override
	public void joinChannel(String channel) {
		super.joinChannel(channel);
		if (isModerator(channel)) {
			TokenBucket modBucket = TokenBuckets.builder()
					.withCapacity(100)
					.withFixedIntervalRefillStrategy(1, 300, TimeUnit.MILLISECONDS)
					.build();
			modMessageBucket.put(channel, modBucket);
		}
	}

	/**
	 * Leaving the channel
	 * @param channel channel name
	 */
	@Override
	public void partChannel(String channel) {
		super.partChannel(channel);

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
