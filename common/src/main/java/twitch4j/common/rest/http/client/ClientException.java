package twitch4j.common.rest.http.client;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import twitch4j.common.json.ErrorResponse;

public class ClientException extends RuntimeException {

	private final HttpResponseStatus status;
	private final HttpHeaders headers;
	private final Mono<ErrorResponse> errorResponse;

	public ClientException(HttpResponseStatus status, HttpHeaders headers, Mono<ErrorResponse> errorResponse) {
		this.status = status;
		this.headers = headers;
		this.errorResponse = errorResponse;
	}

	public HttpResponseStatus getStatus() {
		return status;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public Mono<ErrorResponse> getErrorResponse() {
		return errorResponse;
	}

	@Override
	public String toString() {
		return "ClientException[" +
				"status=" + status +
				", headers=" + headers +
				", errorResponse=" + errorResponse +
				']';
	}
}
