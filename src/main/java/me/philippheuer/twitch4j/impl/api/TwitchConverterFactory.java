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

package me.philippheuer.twitch4j.impl.api;

import lombok.RequiredArgsConstructor;
import me.philippheuer.twitch4j.api.NoScopeInfo;
import me.philippheuer.twitch4j.api.RequiredScope;
import me.philippheuer.twitch4j.auth.ICredential;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

@RequiredArgsConstructor
public class TwitchConverterFactory extends Converter.Factory {
	private final String baseAuth;
	private final JacksonConverterFactory jacksonConverterFactory = JacksonConverterFactory.create();

	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
		return jacksonConverterFactory.responseBodyConverter(type, annotations, retrofit);
	}

	@Override
	public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
		Retrofit newRetrofit = retrofit;
		for (Annotation methodAnnotation : methodAnnotations) {
			if (type instanceof ICredential) {
				if (methodAnnotation instanceof NoScopeInfo || (methodAnnotation instanceof RequiredScope && isRequiredScope((ICredential) type, (RequiredScope) methodAnnotation))) {
					OkHttpClient client = ((OkHttpClient) retrofit.callFactory()).newBuilder()
							.addInterceptor(new HeaderInterceptor("Authorization", String.format("%s %s", baseAuth, ((ICredential) type).getAccessToken())))
							.build();

					newRetrofit = retrofit.newBuilder()
							.client(client)
							.build();
				}
			}
		}

		return jacksonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, newRetrofit);
	}

	private boolean isRequiredScope(ICredential credential, RequiredScope scopes) {
		return credential.getScopes().containsAll(Arrays.asList(scopes.value()));
	}
}
