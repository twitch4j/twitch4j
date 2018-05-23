package twitch4j;

import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import twitch4j.api.TwitchHelix;
import twitch4j.api.TwitchKraken;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Manager;
import twitch4j.irc.TwitchMessageInterface;
import twitch4j.pubsub.PubSubTopic;
import twitch4j.pubsub.TwitchPubSub;

import java.util.Arrays;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class TwitchClient {
	private final TwitchKraken krakenEndpoint;
	private final TwitchHelix helixEndpoint;
	private final Manager credentialManager;
	private final TwitchMessageInterface messageInterface;
	private final TwitchPubSub pubSub;

	public boolean ready() {
		return messageInterface.ready();
	}

	public void login() throws RuntimeException {
		messageInterface.connect();
	}

	public interface Builder {
		SimpleModule getSimpleModule();

		Builder clientId(String clientId);
		Builder clientSecret(String clientSecret);
		Builder userAgent(String userAgent);

		Builder addCredential(ICredential.Builder credential);
		default Builder addCredentials(Collection<ICredential.Builder> credentials) {
			credentials.forEach(this::addCredential);
			return this;
		}
		default Builder addCredentials(ICredential.Builder... credentials) {
			return addCredentials(Arrays.asList(credentials));
		}

		Builder addListener(Object listener);
		default Builder addListeners(Collection<Object> listeners) {
			listeners.forEach(this::addListener);
			return this;
		}
		default Builder addListeners(Object... listeners) {
			return addListeners(Arrays.asList(listeners));
		}

		Builder addTopic(PubSubTopic topic);
		default Builder addTopics(Collection<PubSubTopic> topics) {
			topics.forEach(this::addTopic);
			return this;
		}
		default Builder addTopics(PubSubTopic... topics) {
			return addTopics(Arrays.asList(topics));
		}

		TwitchClient build();

		TwitchClient connect() throws RuntimeException;
	}
}
