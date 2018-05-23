package twitch4j;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import twitch4j.api.TwitchHelix;
import twitch4j.api.TwitchKraken;
import twitch4j.api.util.rest.HeaderRequestInterceptor;
import twitch4j.api.util.rest.RestClient;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.CredentialManager;
import twitch4j.common.auth.storage.AuthStorage;
import twitch4j.common.events.EventManager;
import twitch4j.common.rest.http.*;
import twitch4j.common.rest.http.client.SimpleHttpClient;
import twitch4j.common.rest.request.Router;
import twitch4j.irc.TwitchMessageInterface;
import twitch4j.pubsub.PubSubTopic;
import twitch4j.pubsub.TwitchPubSub;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

@Getter
public class TwitchClient {
	private final TwitchKraken krakenEndpoint;
	private final TwitchHelix helixEndpoint;
	private final CredentialManager credentialManager;
	private final TwitchMessageInterface messageInterface;
	private final TwitchPubSub pubSub;

	TwitchClient(Configuration configuration, AuthStorage storage, EventManager eventManager) {
		krakenEndpoint = buildKrakenEndpoint(configuration);
		helixEndpoint = buildHelixEndpoint(configuration);
		credentialManager = new CredentialManager(configuration, storage);
		messageInterface = new TwitchMessageInterface(configuration, eventManager);
		pubSub = new TwitchPubSub(eventManager, objectMapper());
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

		TwitchClient build();

		TwitchClient connect() throws RuntimeException;
	}

	public static Builder builder() {
		return new TwitchClientBuilder();
	}

	private TwitchHelix buildHelixEndpoint(Configuration configuration) {
		ObjectMapper mapper = objectMapper();

		SimpleHttpClient httpClient = SimpleHttpClient.builder()
				.baseUrl("https://api.twitch.tv/helix")
				.readerStrategy(new JacksonReaderStrategy<>(mapper))
				.readerStrategy(new FallbackReaderStrategy())
				.readerStrategy(new EmptyReaderStrategy())
				.writerStrategy(new JacksonWriterStrategy(mapper))
				.writerStrategy(new EmptyWriterStrategy())
				.build();

		return new TwitchHelix(new Router(httpClient), configuration);
	}

	private ObjectMapper objectMapper() {
		SimpleModule simpleModule = new SimpleModule();

		// Register serialization / deserialization using Simple Module

		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
		mapper.registerModule(simpleModule);

		return mapper;
	}

	private TwitchKraken buildKrakenEndpoint(Configuration configuration) {
		RestClient restClient = new RestClient("https://api.twitch.tv/kraken");
		restClient.addInterceptor(new HeaderRequestInterceptor("Client-ID", configuration.getClientId()));
		restClient.addInterceptor(new HeaderRequestInterceptor("Accept", "application/vnd.twitchtv.v5+json"));
		restClient.addInterceptor(new HeaderRequestInterceptor("User-Agent", configuration.getUserAgent()));

		return new TwitchKraken(restClient.getRestTemplate(objectMapper()));
	}
}
