package twitch4j;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.Instant;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import twitch4j.common.IBotCredential;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;
import twitch4j.common.enums.SubscriptionPlan;
import twitch4j.common.json.converters.InstantClockDeserializer;
import twitch4j.common.json.converters.ScopeDeserializer;
import twitch4j.common.json.converters.SubscriptionPlanDeserializer;
import twitch4j.common.rest.http.EmptyReaderStrategy;
import twitch4j.common.rest.http.EmptyWriterStrategy;
import twitch4j.common.rest.http.FallbackReaderStrategy;
import twitch4j.common.rest.http.JacksonReaderStrategy;
import twitch4j.common.rest.http.JacksonWriterStrategy;
import twitch4j.common.rest.http.client.SimpleHttpClient;
import twitch4j.common.rest.request.Router;

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

	public static Router buildRouter(String baseUrl) {
		ObjectMapper mapper = getMapper();

		SimpleHttpClient httpClient = SimpleHttpClient.builder()
				.baseUrl(baseUrl)
				.readerStrategy(new JacksonReaderStrategy<>(mapper))
				.readerStrategy(new FallbackReaderStrategy())
				.readerStrategy(new EmptyReaderStrategy())
				.writerStrategy(new JacksonWriterStrategy(mapper))
				.writerStrategy(new EmptyWriterStrategy())
				.build();

		return new Router(httpClient);
	}

	public static ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();

		SimpleModule simpleModule = new SimpleModule();

		// Register serialization / deserialization using Simple Module
		simpleModule.addDeserializer(Scope.class, new ScopeDeserializer());
		simpleModule.addDeserializer(Instant.class, new InstantClockDeserializer());
		simpleModule.addDeserializer(SubscriptionPlan.class, new SubscriptionPlanDeserializer());

		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
		mapper.registerModule(simpleModule);

		return mapper;
	}
}
