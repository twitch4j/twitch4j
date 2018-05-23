package twitch4j;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import twitch4j.api.TwitchHelix;
import twitch4j.api.TwitchKraken;
import twitch4j.api.rest.http.*;
import twitch4j.api.rest.http.client.SimpleHttpClient;
import twitch4j.api.rest.request.Router;
import twitch4j.common.events.EventManager;
import twitch4j.common.events.EventSubscriber;
import twitch4j.common.events.IListener;
import twitch4j.pubsub.PubSubTopic;
import twitch4j.api.util.rest.HeaderRequestInterceptor;
import twitch4j.api.util.rest.RestClient;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Manager;
import twitch4j.common.auth.Scope;
import twitch4j.common.auth.storage.AuthStorage;
import twitch4j.common.auth.storage.DefaultAuthStorage;
import twitch4j.common.TwitchBotConfig;
import twitch4j.irc.TwitchMessageInterface;
import twitch4j.pubsub.TwitchPubSub;

import java.util.*;

@Setter
@Accessors(fluent = true, chain = true)
class TwitchClientBuilder implements TwitchClient.Builder {
	private String clientId;
	private String clientSecret;

	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

	private final Set<ICredential.Builder> credentials = new LinkedHashSet<>();

	private final Set<Scope> defaultScopes = new LinkedHashSet<>();
	private boolean forceVerify = false;
	private AuthStorage authenticationStorage = new DefaultAuthStorage();

	private final ObjectMapper mapper = new ObjectMapper();

	@Getter
	@Accessors(fluent = false)
	private final SimpleModule simpleModule = new SimpleModule();

	private final Set<Object> listeners = new LinkedHashSet<>();

	private final Set<PubSubTopic> topics = new LinkedHashSet<>();

	@Override
	public TwitchClient.Builder addCredential(ICredential.Builder credential) {
		this.credentials.add(credential);
		return this;
	}

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
		EventManager eventManager = new EventManager();
		eventManager.registerListeners(listeners);

		mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.registerModule(simpleModule);

		TwitchKraken kraken = releaseTheKraken();
		TwitchHelix helix = newApi();
		Manager manager = Manager.builder().clientId(clientId)
				.clientSecret(clientSecret)
				.forceVerify(forceVerify)
				.scope(defaultScopes)
				.storage(authenticationStorage)
				.build();

		manager.registerCredentials(credentials);

		TwitchMessageInterface tmi = messageInterface(manager.getBotCredential(), eventManager);

		TwitchPubSub pubSub = new TwitchPubSub(eventManager, mapper, topics);

		return new TwitchClient(kraken, helix, manager, tmi, pubSub);
	}

	@Override
	public TwitchClient connect() throws RuntimeException {
		TwitchClient client = build();
		if (!client.getMessageInterface().ready()) {
			throw new RuntimeException("Message Interface is not ready cause you are not initialize bot credential. Define in Credential Builder \"chatBot(true)\"");
		} else {
			client.login();
			return client;
		}
	}

	private boolean hasEventSubscriber(Object listener) {
		return Arrays.stream(listener.getClass().getMethods())
				.anyMatch(m -> m.isAnnotationPresent(EventSubscriber.class));
	}
	private TwitchMessageInterface messageInterface(Optional<ICredential> bot, EventManager eventManager) {
		Optional<TwitchBotConfig> botConfig = bot.map(credential -> new TwitchBotConfig(credential.username(), "oauth:" + credential.accessToken()));

		return new TwitchMessageInterface(botConfig.orElse(null), eventManager);
	}

	private TwitchHelix newApi() {
		SimpleHttpClient httpClient = SimpleHttpClient.builder()
				.baseUrl("https://api.twitch.tv/helix")
				.defaultHeader("Client-ID", clientId)
				.readerStrategy(new JacksonReaderStrategy<>(mapper))
				.readerStrategy(new FallbackReaderStrategy())
				.readerStrategy(new EmptyReaderStrategy())
				.writerStrategy(new JacksonWriterStrategy(mapper))
				.writerStrategy(new EmptyWriterStrategy())
				.build();

		return new TwitchHelix(new Router(httpClient));
	}

	private TwitchKraken releaseTheKraken() {
		RestClient restClient = new RestClient("https://api.twitch.tv/kraken");
		restClient.addInterceptor(new HeaderRequestInterceptor("Client-ID", clientId));
		restClient.addInterceptor(new HeaderRequestInterceptor("Accept", "application/vnd.twitchtv.v5+json"));
		restClient.addInterceptor(new HeaderRequestInterceptor("User-Agent", userAgent));

		return new TwitchKraken(restClient.getRestTemplate(), mapper);
	}
}
