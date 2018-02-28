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

package io.twitch4j.impl.utils;

import io.twitch4j.ITwitchClient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import io.twitch4j.IClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public abstract class TwitchListener implements WebSocketListener {
	private final ITwitchClient client;
	@Setter(AccessLevel.PROTECTED)
	private Optional<Session> session = Optional.empty();

	public boolean isConnected() {
		return getSession().isPresent();
	}

	@Override
	public void onWebSocketBinary(byte[] payload, int offset, int len) {}

	@Override
	public void onWebSocketConnect(Session session) {
		this.session = Optional.of(session);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		this.session = Optional.empty();
	}
}
