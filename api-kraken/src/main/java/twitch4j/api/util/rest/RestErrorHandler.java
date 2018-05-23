package twitch4j.api.util.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import twitch4j.common.json.ErrorResponse;

import java.io.IOException;

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

		ErrorResponse content = mapper.readValue(response.getBody(), ErrorResponse.class);

		if (content.getMessage() == null) {
			content.setMessage(response.getStatusCode().getReasonPhrase());
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
