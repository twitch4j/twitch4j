package me.philippheuer.twitch4j.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class RestErrorHandler implements ResponseErrorHandler {

	private static final Log logger = LogFactory.getLog(RestErrorHandler.class);

	@Override
	public void handleError(ClientHttpResponse clienthttpresponse) throws IOException {
		if (clienthttpresponse.getStatusCode() == HttpStatus.FORBIDDEN) {
			logger.debug(HttpStatus.FORBIDDEN + " response. Throwing authentication exception");
			// throw new AuthenticationException();
		}
	}

	@Override
	public boolean hasError(ClientHttpResponse clienthttpresponse) throws IOException {

		if (clienthttpresponse.getStatusCode() != HttpStatus.OK) {
			String content = convertStreamToString(clienthttpresponse.getBody());

			logger.trace("Status code: " + clienthttpresponse.getStatusCode());
			logger.trace("Response" + clienthttpresponse.getStatusText());
			logger.trace("Content: " + content);

			if (clienthttpresponse.getStatusCode() == HttpStatus.FORBIDDEN) {
				if(content.contains("used Cloudflare to restrict access")) {
					logger.warn("Your current ip is banned by cloudflare, so you can't reach the target.");
				} else {
					logger.debug("Call returned a error 403 forbidden resposne ");
				}

				return true;
			}
		}
		return false;
	}

	/**
	 * Stupid Scanner Tricks ... to convert inputStream to String
	 * @credits: http://stackoverflow.com/a/5445161
	 */
	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
