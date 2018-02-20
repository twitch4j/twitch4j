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

import com.fasterxml.jackson.databind.JsonNode;
import me.philippheuer.twitch4j.api.ResponseException;
import me.philippheuer.twitch4j.utils.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class ExceptionConverter extends CallAdapter.Factory {
	private final Logger logger = LoggerFactory.getLogger(LoggerType.API);

	@Override
	public CallAdapter<JsonNode, Void> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
		return new CallAdapter<JsonNode, Void>() {
			@Override
			public Type responseType() {
				return null;
			}

			@Override
			public Void adapt(Call<JsonNode> call) {
				try {
					Response<JsonNode> response = call.execute();
					if (response.code() >= 400)
						throw new ResponseException(response.code(), response.body().get("message").textValue());
				} catch (ResponseException | IOException ex) {
					logger.error("Exception on API ", ex);
				}
				return null;
			}
		};
	}
}
