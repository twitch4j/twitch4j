package twitch4j.api.util.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import twitch4j.stream.json.Error;

/**
 * Rest Error Handler
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */
public class RestErrorHandler implements ResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		ObjectMapper mapper = createDefaultObjectMapper();

		Error content = mapper.readValue(response.getBody(), Error.class);

		if (content.getError() == null) {
			content.setError(response.getStatusCode().getReasonPhrase());
		}

		if (content.getStatus() == null) {
			content.setStatus(response.getStatusCode().value());
		}

		throw new RestException(content);
	}

	private ObjectMapper createDefaultObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);

		return mapper;
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return (response.getStatusCode().is4xxClientError() && response.getStatusCode().is5xxServerError());
	}

}
