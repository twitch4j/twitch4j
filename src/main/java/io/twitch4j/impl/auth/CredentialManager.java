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

package io.twitch4j.impl.auth;

import io.twitch4j.ITwitchClient;
import io.twitch4j.TwitchBuilder;
import io.twitch4j.enums.TwitchComponents;
import lombok.Getter;
import lombok.Setter;
import io.twitch4j.auth.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CredentialManager implements IManager {
	private final ITwitchClient client;

	private final Logger logger = LoggerFactory.getLogger(TwitchComponents.CREDENTIAL_MANAGER);

	@Getter
	@Setter
	private ICredentialStorage credentialStorage;

	private final IAuthorization authorization;

	public CredentialManager(ITwitchClient client) {
		this.client = client;
		this.authorization = new Authorization(client);
	}

	@Override
	public String buildAuthorizationUrl(Set<Scope> scopes, String redirectUrl, String state) {
		return authorization.buildAuthorizationUrl(scopes, redirectUrl, state);
	}

	@Override
	public ICredential generateAccessToken(String code, String redirectUrl) throws Exception {
		ICredential credential = authorization.buildAccessToken(code, redirectUrl);
		credentialStorage.add(credential);
		return credential;
	}

	@Override
	public ICredential refreshToken(ICredential credential) throws Exception {
		ICredential newCredential = authorization.refreshToken(credential);
		credentialStorage.remove(credential);
		credentialStorage.add(newCredential);
		return newCredential;
	}

	@Override
	public void revokeToken(ICredential credential) throws Exception {
		if (authorization.revokeToken(credential)) {
			credentialStorage.remove(credential);
		}
	}

	@Override
	public void revokeToken(ICredential credential, boolean revokeOnly) throws Exception {
		authorization.revokeToken(credential);
	}

	@Override
	public ICredential rebuildCredentialData(ICredential credential) throws Exception {
		return null;
	}

	@Override
	public ICredential buildCredentialData(TwitchBuilder.Credentials credentialBuilder) throws Exception {
		return authorization.buildCredentialData(credentialBuilder);
	}
}
