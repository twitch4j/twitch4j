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

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.impl.auth.converters.ExpiredTimestamp;
import io.twitch4j.impl.auth.converters.JWTSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.Scope;
import io.twitch4j.impl.auth.converters.ExpiredTimestamp;
import io.twitch4j.impl.auth.converters.JWTSerialize;

import java.util.Calendar;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Credential implements ICredential {
	private final String accessToken;
	private final String refreshToken;
	@JsonSerialize(converter = JWTSerialize.class)
	private DecodedJWT idToken;
	@JsonSerialize(converter = ExpiredTimestamp.class)
	private Calendar expiredAt;
	private Set<Scope> scopes;
	private User user;

	public Credential(String accessToken, String refreshToken, DecodedJWT idToken) {
		this(accessToken, refreshToken);
		this.idToken = idToken;
	}

	@Override
	public Calendar expiredAt() {
		return expiredAt;
	}
}
