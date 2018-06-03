package twitch4j.common.rest.http.client;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import reactor.ipc.netty.http.client.HttpClient;
import twitch4j.common.rest.http.ReaderStrategy;
import twitch4j.common.rest.http.WriterStrategy;

class SimpleHttpClientBuilder implements SimpleHttpClient.Builder {

	private final HttpHeaders headers = new DefaultHttpHeaders();
	private final List<ReaderStrategy<?>> readerStrategies = new ArrayList<>();
	private final List<WriterStrategy<?>> writerStrategies = new ArrayList<>();
	private String baseUrl = "";

	@Override
	public SimpleHttpClient.Builder baseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	@Override
	public SimpleHttpClient.Builder defaultHeader(String key, String value) {
		headers.add(key, value);
		return this;
	}

	@Override
	public SimpleHttpClient.Builder writerStrategy(WriterStrategy<?> strategy) {
		writerStrategies.add(strategy);
		return this;
	}

	@Override
	public SimpleHttpClient.Builder readerStrategy(ReaderStrategy<?> strategy) {
		readerStrategies.add(strategy);
		return this;
	}

	@Override
	public SimpleHttpClient build() {
		return new SimpleHttpClient(HttpClient.create(options -> options.compression(true)), baseUrl, headers, writerStrategies, readerStrategies);
	}
}
