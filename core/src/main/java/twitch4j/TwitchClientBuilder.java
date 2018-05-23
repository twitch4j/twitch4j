package twitch4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import twitch4j.common.auth.AuthService;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;
import twitch4j.common.auth.storage.AuthStorage;
import twitch4j.common.auth.storage.DefaultAuthStorage;
import twitch4j.common.events.EventManager;
import twitch4j.common.events.EventSubscriber;
import twitch4j.common.events.IListener;
import twitch4j.pubsub.PubSubTopic;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Setter
@Accessors(fluent = true, chain = true)
class TwitchClientBuilder implements TwitchClient.Builder {
	private String clientId;
	private String clientSecret;

	private Supplier<String> redirectUri = () -> "http://localhost:8080";

	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

	private ICredential.Builder botCredential;

	private final Set<Scope> defaultScopes = new LinkedHashSet<>();
	private boolean forceVerify = false;
	private AuthStorage authenticationStorage = new DefaultAuthStorage();

	private final Set<Object> listeners = new LinkedHashSet<>();

	private final Set<PubSubTopic> topics = new LinkedHashSet<>();

	@Override
	public TwitchClient.Builder addListener(Object listener) {
		if (hasEventSubscriber(listener) || IListener.class.isAssignableFrom(listener.getClass())) {
			listeners.add(listener);
		}
		return this;
	}

	@Override
	public TwitchClient.Builder addTopic(PubSubTopic topic) {
		topics.add(topic);
		return this;
	}

	@Override
	public TwitchClient build() {
		Configuration configuration = new Configuration(clientId, clientSecret, userAgent, redirectUri.get(), defaultScopes, forceVerify);
		if (botCredential != null) {
			AuthService service = new AuthService(configuration);
			ICredential botCredential = this.botCredential.build(service);
			configuration.setBotCredentials(botCredential);
		}

		EventManager eventManager = new EventManager();
		eventManager.registerListeners(listeners);

		return new TwitchClient(configuration, authenticationStorage, eventManager);
	}

	@Override
	public TwitchClient connect() throws RuntimeException {
		TwitchClient client = build();
		if (!client.getMessageInterface().ready()) {
			throw new RuntimeException("Message Interface is not ready cause you are not initialize bot credential. Define in Credential Builder \"chatBot(true)\"");
		}

		client.getMessageInterface().connect();
		client.getPubSub().connect();
		return client;
	}

	private boolean hasEventSubscriber(Object listener) {
		return Arrays.stream(listener.getClass().getMethods())
				.anyMatch(m -> m.isAnnotationPresent(EventSubscriber.class));
	}
}
