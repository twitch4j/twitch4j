/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.twitch4j.impl.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.twitch4j.ITwitchClient;
import io.twitch4j.enums.TwitchEndpoint;
import io.twitch4j.api.IApi;
import io.twitch4j.api.Model;
import io.twitch4j.api.RequestType;
import lombok.AccessLevel;
import lombok.Getter;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

@Getter
public class Api implements IApi {

	private final ITwitchClient client;

	private final OkHttpClient httpClient;

	protected final ObjectMapper mapper;

	@Getter(AccessLevel.NONE)
	private TwitchEndpoint api;

	protected Api(ITwitchClient client, TwitchEndpoint api) {
		this.client = client;
		this.api = (api.equals(TwitchEndpoint.HELIX) || api.equals(TwitchEndpoint.KRAKEN)) ? api : TwitchEndpoint.DEFAULT;
		this.httpClient = new OkHttpClient.Builder()
				.addInterceptor(new TwitchInterceptor(client, true))
				.build();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
		mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);

		this.mapper = mapper;
	}

	@Override
	public Response request(RequestType requestType, String url, RequestBody body, Map<String, String> headers) throws IOException {
		Request.Builder request = new Request.Builder();
		request.url(url);
		if (!headers.isEmpty()) {
			request.headers(Headers.of(headers));
		}
		request.method(requestType.name(), body);
		try (Response response = httpClient.newCall(request.build()).execute()) {
			return response;
		}
	}

	public <T> T buildPOJO(Response response, Class<T> responseType) throws IOException {
		T data = mapper.readValue(response.body().bytes(), responseType);

		if (data instanceof Model) {
			((Model) data).setClient(client);
		}

		return data;
	}

	@Override
	public <T> T buildPOJO(Response response, TypeReference<T> responseType) throws IOException {
		T data = mapper.readValue(response.body().bytes(), responseType);

		if (data instanceof Model) {
			((Model) data).setClient(client);
		}

		return data;
	}
}
