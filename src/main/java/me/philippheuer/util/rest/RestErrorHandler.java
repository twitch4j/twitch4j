package me.philippheuer.util.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.log.Logger;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.RestError;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

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
	public void handleError(ClientHttpResponse clienthttpresponse) throws IOException {
		if (clienthttpresponse.getStatusCode() == HttpStatus.FORBIDDEN) {
			Logger.debug(this, HttpStatus.FORBIDDEN + " response. Throwing authentication exception");
			// throw new AuthenticationException();
		}
	}

	@Override
	public boolean hasError(ClientHttpResponse clienthttpresponse) throws IOException {

		if (clienthttpresponse.getStatusCode() != HttpStatus.OK) {
			String content = IOUtils.toString(clienthttpresponse.getBody(), "UTF-8");

			// The server has successfully fulfilled the request and that there is no additional content to send in the response payload body.
			if(clienthttpresponse.getStatusCode() == HttpStatus.NO_CONTENT) {
				return false;
			}

			// JSON String to RestError
			try {
				// REST Error
				ObjectMapper mapper = new ObjectMapper();
				RestError restError = mapper.readValue(content, RestError.class);

				// Add HTTP Status Code to Error
				if (restError.getStatus() == null) {
					restError.setStatus(clienthttpresponse.getStatusCode().ordinal());
				}

				throw new RestException(restError);
				
			} catch (RestException restException) {
				// Rethrow
				throw restException;
			} catch (Exception ex) {

				// No REST Error
				Logger.trace(this, "Status code: " + clienthttpresponse.getStatusCode());
				Logger.trace(this, "Response" + clienthttpresponse.getStatusText());
				Logger.trace(this, "Content: " + content);

				if (clienthttpresponse.getStatusCode() == HttpStatus.FORBIDDEN) {
					if (content.contains("used Cloudflare to restrict access")) {
						Logger.warn(this, "Your current ip is banned by cloudflare, so you can't reach the target.");
					} else {
						Logger.debug(this, "Call returned a error 403 forbidden resposne ");
					}

					return true;
				}

			}
		}
		return false;
	}

}
