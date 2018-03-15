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

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.twitch4j.api.kraken.model.User;
import io.twitch4j.enums.Scope;
import io.twitch4j.impl.auth.CredentialBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.Set;

/**
 * Credentials for specific user. Can be build using {@link Builder} defining only {@link Builder#accessToken(String)} and {@link Builder#refreshToken(String)}
 *
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public interface ICredential {

    /**
     * User authorization Access Token
     *
     * @return Access Token
     */
    String getAccessToken();

    /**
     * User authorization Refresh Token
     *
     * @return Refresh Token
     */
    String getRefreshToken();

    /**
     * ID Token when {@link Scope#OPENID} is exists on {@link #getScopes()}. Can be nullable if you not authorized required scope.
     *
     * @return ID Token
     */
    DecodedJWT getIdToken();

    /**
     * Timestamped expiration credential. Can be nullable if credentials has been added manually.
     *
     * @return Expiration date
     */
    Instant expiredAt();

    /**
     * Authorized Scopes
     *
     * @return Scopes
     */
    Set<Scope> getScopes();

    /**
     * User registered via {@link #getAccessToken()}
     *
     * @return Registered user
     */
    User getUser();

    @Value.Check
    default void check() {
        Validate.validState(StringUtils.isAllBlank(getAccessToken()), "Required Access Token");
        Validate.validState(StringUtils.isAllBlank(getRefreshToken()), "Required Refresh Token");
    }

    class Builder extends CredentialBuilder {
        public Builder idToken(String idToken) {
            return idToken(JWT.decode(idToken));
        }
    }
}
