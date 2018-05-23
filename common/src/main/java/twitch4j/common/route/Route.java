package twitch4j.common.route;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import okhttp3.Headers;
import twitch4j.common.http.*;
import twitch4j.common.http.client.HttpClient;
import twitch4j.common.http.request.TwitchRequest;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Route {
	private final String baseUrl;

	public static Route create(String baseUrl) {
		return new Route(baseUrl);
	}

	public TwitchRequest get() {
		return new TwitchRequest(this, HttpMethod.GET, null);
	}






	private static List<ReaderStrategy<?>> defaultReaderStategies() {
		List<ReaderStrategy<?>> strategies = new ArrayList<>();
		ObjectMapper mapper = createDefaultMapper();
		strategies.add(new JacksonReaderStrategy<>(mapper));
		strategies.add(new EmptyReaderStrategy());
		strategies.add(new FallbackReaderStrategy());

		return strategies;
	}

	private static List<WriterStrategy<?>> defaultWriterStrategies() {
		List<WriterStrategy<?>> strategies = new ArrayList<>();
		ObjectMapper mapper = createDefaultMapper();
		strategies.add(new JacksonWriterStrategy(mapper));
		strategies.add(new EmptyWriterStrategy());

		return strategies;
	}

	private static ObjectMapper createDefaultMapper() {
		SimpleModule simpleModule = new SimpleModule();

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.registerModule(simpleModule);

		return mapper;
	}
}
