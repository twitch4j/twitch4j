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

package io.twitch4j.api.kraken.operations;

import io.twitch4j.api.FindOne;
import io.twitch4j.api.OnField;
import io.twitch4j.api.RequiredScope;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.Scope;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Optional;

public interface UserKrakenOperation extends KrakenOperation<User, Long> {
	@Override
	@GET("/user")
	@RequiredScope(Scope.USER_READ)
	User get(ICredential credential);

	@Override
	@GET("/users/{id}")
	User getById(@Path("id") Long userId);

	@FindOne
	@Override
	@GET("/users")
	@OnField("users")
	Optional<User> getByName(@Query("login") String name);
}
