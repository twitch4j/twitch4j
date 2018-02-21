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

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import io.twitch4j.IClient;
import io.twitch4j.auth.ICredential;
import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

@RequiredArgsConstructor
public class CredentialRowMapper implements RowMapper<ICredential> {

	private final IClient client;

	@Override
	public ICredential map(ResultSet rs, StatementContext ctx) throws SQLException {
		DecodedJWT jwt = (StringUtils.isBlank(rs.getString("id_token"))) ? null : JWT.decode(rs.getString("id_token"));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rs.getDate("expired_at"));
		Credential credential = new Credential(
				rs.getString("access_token"),
				rs.getString("refresh_token"),
				jwt,
				calendar,
				null,
				null
		);
		return fetchMissingData(credential);
	}

	private ICredential fetchMissingData(Credential credential) {
		return client.getCredentialManager().buildCredentialData(credential);
	}
}
