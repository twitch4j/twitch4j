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

import io.twitch4j.api.IApi;
import io.twitch4j.utils.LoggerType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.twitch4j.IClient;
import io.twitch4j.api.IApi;
import io.twitch4j.utils.LoggerType;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Slf4j(topic = LoggerType.API)
public class Api implements IApi {
	@Getter(AccessLevel.PROTECTED)
	private final IClient client;
	private final Retrofit retrofit;
	@Getter
	private final String baseAuth;

	public Api(IClient client, String baseUrl, String baseAuth) {
		OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
				.addInterceptor(new HeaderInterceptor("Client-ID", client.getConfiguration().getClientId()))
				.addInterceptor(new HeaderInterceptor("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"));
		if (baseUrl.contains("kraken")) {
			httpClient.addInterceptor(new HeaderInterceptor("Accept", "application/vnd.twitchtv.v5+json"));
		}
		this.client = client;
		this.baseAuth = baseAuth;
		this.retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addCallAdapterFactory(new ExceptionConverter())
				.addCallAdapterFactory(new TwitchCallAdapterFactory(client))
				.addConverterFactory(new TwitchConverterFactory(baseAuth))
				.client(httpClient.build())
				.build();
	}


	@Override
	public <S> S createService(final Class<S> serviceClass) {
		return retrofit.create(serviceClass);
	}
}
