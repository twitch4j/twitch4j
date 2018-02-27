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

package io.twitch4j.impl.api.kraken.operations;

import io.twitch4j.TwitchAPI;
import io.twitch4j.impl.api.Api;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.api.kraken.operations.Users;
import io.twitch4j.auth.ICredential;
import io.twitch4j.impl.api.Operation;
import okhttp3.Response;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class UsersOperation extends Operation implements Users {
	public UsersOperation(Api api) {
		super(api);
	}

	@Override
	public User get(ICredential credential) {
		if (!Objects.isNull(credential.getUser())) {
			return credential.getUser();
		}
		String url = String.format("%suser", TwitchAPI.KRAKEN.getUrl());

		Response response = getApi().get(url, Collections.singletonMap("Authorization", String.format("%s %s", IKraken.PREFIX_AUTHORIZATION, credential.getAccessToken())));

		return getApi().buildPOJO(response, User.class);
	}

	@Override
	public User getById(Long id) {
		String url = String.format("%susers/%d", TwitchAPI.KRAKEN.getUrl(), id);
		Response response = getApi().get(url);
		return getApi().buildPOJO(response, User.class);
	}

	@Override
	public Optional<User> getByName(String name) {
		String url = String.format("%susers?login=%s", TwitchAPI.KRAKEN.getUrl(), name);
		Response response = getApi().get(url);
		return Optional.of(getApi().buildPOJO(response, User.class));
	}
}
