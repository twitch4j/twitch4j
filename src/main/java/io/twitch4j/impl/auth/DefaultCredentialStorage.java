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

import io.twitch4j.api.kraken.models.User;
import io.twitch4j.IClient;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.ICredentialStorage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultCredentialStorage implements ICredentialStorage {

	private final IClient client;
	private final Jdbi jdbi = Jdbi.create("jdbc:h2:./data");
	private Handle handle;

	public DefaultCredentialStorage(IClient client) {
		this.client = client;
	}

	@Override
	public Set<ICredential> getCredentials() {
		return handle.createQuery("SELECT * FROM `credentials`")
				.map(new CredentialRowMapper(client)).collect(Collectors.toSet());
	}

	@Override
	public Optional<ICredential> getCredentialById(long userId) {
		return handle.createQuery("SELECT * FROM `credentials` WHERE user_id = :id")
				.bind("id", userId)
				.map(new CredentialRowMapper(client))
				.findFirst();
	}

	@Override
	public Optional<ICredential> getCredentialName(String username) {
		Optional<User> user = client.getKrakenApi().userOperation().getByName(username);
		if (user.isPresent()) {
			return getCredentialById(user.get().getId());
		} else return Optional.empty();
	}

	@Override
	public Optional<ICredential> getCredential(User user) {
		return getCredentialById(user.getId());
	}

	@Override
	public void add(ICredential credential) {
		handle.createUpdate("INSERT INTO `credentials` " +
				"(user_id, access_token, refresh_token, id_token, expired_at) " +
				"VALUES (:id, :access_token, :refresh_token, :id_token, :expired_at)")
				.bind("id", credential.getUser().getId())
				.bind("access_token", credential.getAccessToken())
				.bind("refresh_token", credential.getRefreshToken())
				.bind("id_token", (credential.getIdToken() != null) ? credential.getIdToken().getToken() : null)
				.bind("expired_at", credential.expiredAt().getTime())
				.execute();
	}

	@Override
	public void remove(ICredential credential) {
		handle.createUpdate("DELETE FROM `credentials` WHERE user_id = :id")
				.bind("id", credential.getUser().getId())
				.execute();
	}

	@Override
	public void remove(ICredential credential, boolean revoke) {
		remove(credential);
		if (revoke) {
			client.getCredentialManager().revokeToken(credential, true);
		}
	}

	@Override
	public boolean isConnected() {
		return !handle.isClosed();
	}

	@Override
	public void connect() {
		this.handle = jdbi.open();
		this.handle
				.createUpdate("CREATE TABLE IF NOT EXISTS `credentials` ( user_id INT PRIMARY KEY, access_token VARCHAR, refresh_token VARCHAR, id_token VARCHAR, expired_at DATETIME )")
				.execute();
		this.handle
				.createUpdate("CREATE UNIQUE INDEX IF NOT EXISTS credentials_user_id_uindex ON credentials (user_id)")
				.execute();
	}

	@Override
	public void disconnect() {
		this.handle.commit().close();
	}

	@Override
	public void reconnect() {
		disconnect();
		connect();
	}
}
