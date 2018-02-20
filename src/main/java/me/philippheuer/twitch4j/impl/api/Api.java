package me.philippheuer.twitch4j.impl.api;

import lombok.AccessLevel;
import lombok.Getter;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.api.IApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Api implements IApi {
	@Getter(AccessLevel.PROTECTED)
	private final IClient client;
	private final Retrofit retrofit;
	@Getter
	private final String baseAuth;

	public Api(IClient client, String baseUrl, String baseAuth) {
		OkHttpClient httpClient = new OkHttpClient.Builder()
				.addInterceptor(new HeaderInterceptor("Client-ID", client.getConfiguration().getClientId()))
				.addInterceptor(new HeaderInterceptor("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"))
				.addInterceptor(new HeaderInterceptor("Accept", (baseUrl.contains("kraken")) ? "application/vnd.twitchtv.v5+json" : "application/json"))
				.build();

		this.client = client;
		this.baseAuth = baseAuth;
		this.retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(new TwitchConverterFactory(baseAuth))
				.client(httpClient)
				.build();
	}


	@Override
	public <S> S createService(final Class<S> serviceClass) {
		return retrofit.create(serviceClass);
	}
}
