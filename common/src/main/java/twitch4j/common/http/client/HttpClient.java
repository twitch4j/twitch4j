package twitch4j.common.http.client;

import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import twitch4j.common.http.HttpMethod;
import twitch4j.common.http.ReaderStrategy;
import twitch4j.common.http.WriterStrategy;
import twitch4j.common.json.ErrorResponse;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClient {
	private final OkHttpClient httpClient;
	private final Headers headers;
	private final List<ReaderStrategy<?>> readerStrategies;
	private final List<WriterStrategy<?>> writerStrategies;

	public <R, T> Single<T> exchange(HttpMethod method, String uri, @Nullable R body, Class<T> responseType, ExchangeFilter exchangeFilter) {
		return Single.defer(() -> {
			Headers headers = (!this.headers.toMultimap().containsKey("Content-Type"))
					? this.headers.newBuilder().add("Content-Type", "application/json").build()
					: this.headers;

			String contentType = headers.get("Content-Type");
			Request.Builder requestBuilder = new Request.Builder()
					.method(method.name(), writerStrategies.stream()
							.filter(s -> s.canWrite((body != null) ? body.getClass() : null, contentType))
							.findFirst()
							.map(HttpClient::<R>cast)
							.map(s -> {
								try {
									return s.write(contentType, body);
								} catch (IOException e) {
									throw Exceptions.propagate(e);
								}
							})
							.orElse(null))
					.headers(headers)
					.url(uri);

			exchangeFilter.getRequestFilter().accept(requestBuilder);

			return request(requestBuilder.build(), responseType, exchangeFilter.getResponseFilter());
		});

	}

	@SuppressWarnings("unchecked")
	private <T> Single<T> request(Request request, Class<T> responseType, Consumer<Response> responseFilter) {
		return Single.create(sub -> {
			try (Response response = httpClient.newCall(request).execute()) {
				responseFilter.accept(response);
				String contentType = response.header("Content-Type");
				Optional<ReaderStrategy<?>> readerStrategy = readerStrategies.stream()
						.filter(s -> s.canRead(responseType, contentType))
						.findFirst();
				int statusCode = response.code();
				if (statusCode >= 400 && statusCode < 600) {
					readerStrategy.map(HttpClient::<ErrorResponse>cast)
							.ifPresent(rs -> {
								try {
									sub.onError(clientException(response, rs.read(response, ErrorResponse.class)));
								} catch (IOException e) {
									sub.onError(e);
								}
							});
				} else {
					readerStrategy.map(HttpClient::<T>cast)
							.ifPresent(rs -> {
								try {
									sub.onSuccess(rs.read(response, responseType));
								} catch (IOException e) {
									sub.onError(e);
								}
							});
				}

				if (!sub.isDisposed()) {
					sub.onError(new RuntimeException("No strategies to read this response: " +
											responseType + " - " + contentType));
				}
			} catch (IOException e) {
				sub.onError(e);
			}
		});
	}

	private HttpClientException clientException(Response response, ErrorResponse errorResponse) {
		return new HttpClientException(buildStatus(response), response.headers(), errorResponse);
	}

	private HttpResponseStatus buildStatus(Response response) {
		return new HttpResponseStatus(response.code(), response.message());
	}

	@SuppressWarnings("unchecked")
	private static <T> WriterStrategy<T> cast(WriterStrategy<?> strategy) {
		return (WriterStrategy<T>) strategy;
	}

	@SuppressWarnings("unchecked")
	private static <T> ReaderStrategy<T> cast(ReaderStrategy<?> strategy) {
		return (ReaderStrategy<T>) strategy;
	}
}
