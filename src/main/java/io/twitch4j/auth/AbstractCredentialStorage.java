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

package io.twitch4j.auth;

import io.twitch4j.Builder;
import io.twitch4j.IClient;
import io.twitch4j.api.kraken.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Calendar;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public abstract class AbstractCredentialStorage implements ICredentialStorage {
	private final IClient client;
	private final IManager credentialManager = client.getCredentialManager();

	@Override
	public Optional<ICredential> getCredentialById(long userId) {
		return getCredentials().stream()
				.filter(credential -> credential.getUser().getId().equals(userId))
				.findFirst();
	}

	@Override
	public Optional<ICredential> getCredentialName(String username) {
		return getCredentials().stream()
				.filter(credential -> credential.getUser().getUsername().equals(username))
				.findFirst();
	}

	@Override
	public Optional<ICredential> getCredential(User user) {
		return getCredentialById(user.getId());
	}

	@Override
	public void add(ICredential credential) throws Exception {
		ICredential preCredential;
		if (ObjectUtils.allNotNull(credential.expiredAt(), credential.getScopes(), credential.getUser())) {
			preCredential = client.getCredentialManager().buildCredentialData(
					Builder.newCredential()
						.withAccessToken(credential.getAccessToken())
						.withRefreshToken(credential.getRefreshToken())
						.withIdToken(credential.getIdToken())
			);
		} else preCredential = credential;
		getCredentials().add(preCredential);
	}

	@Override
	public void remove(ICredential credential) throws Exception {
		remove(credential, false);
	}

	@Override
	public void remove(ICredential credential, boolean revoke) throws Exception {
		if (revoke) {
			client.getCredentialManager().revokeToken(credential);
		}
		getCredentials().remove(credential);
	}
}
