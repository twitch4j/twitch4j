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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.twitch4j.IClient;
import io.twitch4j.api.Model;
import io.twitch4j.utils.LoggerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.twitch4j.IClient;
import io.twitch4j.api.Model;
import io.twitch4j.utils.LoggerType;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Slf4j(topic = LoggerType.API)
@RequiredArgsConstructor
public class TwitchCallAdapterFactory<T extends Model> extends CallAdapter.Factory {
	private final IClient client;

	@Override
	public CallAdapter<JsonNode, T> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
		return new CallAdapter<JsonNode, T>() {
			@Override
			public Type responseType() {
				return returnType;
			}

			@Override
			public T adapt(Call<JsonNode> call) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					Response<JsonNode> response = call.execute();
					if (response.code() > 200) {
						JavaType type = mapper.constructType(returnType);
						T ret = mapper.convertValue(response.body(), type);
						ret.setClient(client);
					}
				} catch (IOException e) {
					TwitchCallAdapterFactory.log.error("Cannot execute call ", e);
				}
				return null;
			}
		};
	}
}
