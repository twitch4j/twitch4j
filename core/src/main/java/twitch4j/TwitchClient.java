package twitch4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import com.github.philippheuer.events4j.EventManager;
import lombok.Getter;
import twitch4j.api.TwitchHelix;
import twitch4j.api.TwitchKraken;
import twitch4j.common.auth.AuthService;
import twitch4j.common.auth.CredentialManager;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.storage.AuthStorage;
import twitch4j.common.utils.CommonUtils;
import twitch4j.irc.MessageInterfaceAPI;
import twitch4j.irc.TwitchMessageInterface;
import twitch4j.pubsub.PubSubTopic;
import twitch4j.pubsub.TwitchPubSub;

@Getter
public class TwitchClient {
	private final TwitchKraken krakenEndpoint;
	private final TwitchHelix helixEndpoint;
	private final CredentialManager credentialManager;
	private final TwitchMessageInterface messageInterface;
	private final TwitchPubSub pubSub;

	TwitchClient(Configuration configuration, AuthService service, AuthStorage storage, MessageInterfaceAPI tmiApi, EventManager eventManager) {
		krakenEndpoint = new TwitchKraken(configuration);
		helixEndpoint = new TwitchHelix(configuration);
		credentialManager = new CredentialManager(service, storage);
		messageInterface = new TwitchMessageInterface(configuration, eventManager, tmiApi);
		pubSub = new TwitchPubSub(eventManager, CommonUtils.getMapper());
	}

	public static Builder builder() {
		return new TwitchClientBuilder();
	}

	public interface Builder {
		Builder clientId(String clientId);

		Builder clientSecret(String clientSecret);

		Builder userAgent(String userAgent);

		Builder botCredential(ICredential.Builder credential);

		Builder redirectUri(Supplier<String> uri);

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

		Builder addChannel(String channel);

		default Builder addChannels(Collection<String> channels) {
			channels.forEach(this::addChannel);
			return this;
		}

		default Builder addChannels(String... channels) {
			return addChannels(Arrays.asList(channels));
		}

		TwitchClient build();

		TwitchClient connect() throws RuntimeException;
	}
}
