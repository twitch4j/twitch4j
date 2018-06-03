package twitch4j;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import lombok.Setter;
import lombok.experimental.Accessors;
import twitch4j.common.BotCredentialImpl;
import twitch4j.common.auth.AuthService;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;
import twitch4j.common.auth.storage.AuthStorage;
import twitch4j.common.auth.storage.DefaultAuthStorage;
import twitch4j.common.events.EventManager;
import twitch4j.common.events.EventSubscriber;
import twitch4j.common.events.IListener;
import twitch4j.irc.MessageInterfaceAPI;
import twitch4j.pubsub.PubSubTopic;

@Setter
@Accessors(fluent = true, chain = true)
class TwitchClientBuilder implements TwitchClient.Builder {
	private final Set<Scope> defaultScopes = new LinkedHashSet<>();
	private final Set<Object> listeners = new LinkedHashSet<>();
	private final Set<PubSubTopic> topics = new LinkedHashSet<>();
	private final Set<String> channels = new LinkedHashSet<>();
	private String clientId;
	private String clientSecret;
	private Supplier<String> redirectUri = () -> "http://localhost:8080";
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private ICredential.Builder botCredential;
	private boolean forceVerify = false;
	private AuthStorage authenticationStorage = new DefaultAuthStorage();

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
	public TwitchClient.Builder addChannel(String channel) {
		channels.add(channel);
		return this;
	}

	@Override
	public TwitchClient build() {
		Configuration configuration = new Configuration(clientId, clientSecret, userAgent, redirectUri.get(), defaultScopes, forceVerify);
		AuthService service = new AuthService(configuration, Configuration.buildRouter("https://id.twitch.tv/oauth2"));
		MessageInterfaceAPI tmiApi = new MessageInterfaceAPI(configuration);

		if (botCredential != null) {
			ICredential botCredential = this.botCredential.build(service);
			tmiApi.getUserChat(botCredential.userId()).subscribe(bot -> {
				configuration.setBotCredentials(new BotCredentialImpl(botCredential, bot.isKnownBot(), bot.isVerifiedBot()));
			});
		}

		EventManager eventManager = new EventManager();
		eventManager.registerListeners(listeners);

		return new TwitchClient(configuration, service, authenticationStorage, tmiApi, eventManager);
	}

	@Override
	public TwitchClient connect() throws RuntimeException {
		TwitchClient client = build();
		if (!client.getMessageInterface().ready()) {
			throw new RuntimeException("Message Interface is not ready cause you are not initialize bot credential. Define in Credential Builder \"chatBot(true)\"");
		}

		client.getMessageInterface().connect();
		client.getPubSub().connect();

		if (!channels.isEmpty()) {
			channels.forEach(client.getMessageInterface()::join);
		}
		return client;
	}

	private boolean hasEventSubscriber(Object listener) {
		return Arrays.stream(listener.getClass().getMethods())
				.anyMatch(m -> m.isAnnotationPresent(EventSubscriber.class));
	}
}
