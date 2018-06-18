package twitch4j;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import twitch4j.common.IBotCredential;
import twitch4j.common.auth.Scope;
import twitch4j.common.enums.*;
import twitch4j.common.json.converters.*;
import twitch4j.stream.rest.http.EmptyReaderStrategy;
import twitch4j.stream.rest.http.EmptyWriterStrategy;
import twitch4j.stream.rest.http.FallbackReaderStrategy;
import twitch4j.stream.rest.http.JacksonReaderStrategy;
import twitch4j.stream.rest.http.JacksonWriterStrategy;
import twitch4j.stream.rest.http.client.SimpleHttpClient;
import twitch4j.stream.rest.request.Router;

@Data
@Setter(AccessLevel.PACKAGE)
public class Configuration {
	private final String clientId;
	private final String clientSecret;
	private final String userAgent;
	private final String redirectUri;
	private final Set<Scope> defaultScopes;
	private final boolean forceVerify;
	@Nullable
	private IBotCredential botCredentials;

	public static Router buildRouter(String baseUrl, Map<String, String> headers) {
		ObjectMapper mapper = getMapper();

		SimpleHttpClient.Builder httpClient = SimpleHttpClient.builder()
				.baseUrl(baseUrl)
				.readerStrategy(new JacksonReaderStrategy<>(mapper))
				.readerStrategy(new FallbackReaderStrategy())
				.readerStrategy(new EmptyReaderStrategy())
				.writerStrategy(new JacksonWriterStrategy(mapper))
				.writerStrategy(new EmptyWriterStrategy());

		headers.forEach(httpClient::defaultHeader);

		return new Router(httpClient.build());
	}

	public static ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();

		SimpleModule simpleModule = new SimpleModule();

		// Register serialization / deserialization using Simple Module
		simpleModule.addDeserializer(BroadcasterType.class, new BroadcasterTypeDeserializer())
				.addDeserializer(Duration.class, new DurationDeserializer())
				.addDeserializer(Instant.class, new InstantClockDeserializer())
				.addDeserializer(Scope.class, new ScopeDeserializer())
				.addDeserializer(StreamType.class, new StreamTypeDeserializer())
				.addDeserializer(SubscriptionPlan.class, new SubscriptionPlanDeserializer())
				.addDeserializer(UserType.class, new UserTypeDeserializer())
				.addDeserializer(VideoAccess.class, new VideoAccessDeserializer())
				.addDeserializer(VideoType.class, new VideoTypeDeserializer());

		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
		mapper.registerModule(simpleModule);

		return mapper;
	}
}
