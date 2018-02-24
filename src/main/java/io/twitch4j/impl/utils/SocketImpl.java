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

import io.twitch4j.TwitchAPI;
import io.twitch4j.utils.ISocket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.twitch4j.IClient;
import io.twitch4j.utils.ISocket;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class SocketImpl<T extends TwitchListener> implements ISocket {
	private final IClient client;
	private final WebSocketClient wsClient = new WebSocketClient();
	private final T listener;
	private final TwitchAPI uri;

	public Optional<Session> getSession() {
		return listener.getSession();
	}

	@Override
	public boolean isConnected() {
		return listener.isConnected();
	}

	@Override
	public void connect() throws Exception {
		if (!isConnected()) {
			wsClient.start();
			wsClient.setStopAtShutdown(true);
			wsClient.connect(listener, new URI(uri.getUrl()), new ClientUpgradeRequest());
		}
	}

	@Override
	public void disconnect() throws Exception {
		if (isConnected()) {
			getSession().get().disconnect();
			wsClient.stop();
		}
	}

	@Override
	public void reconnect() throws Exception {
		wsClient.setStopAtShutdown(false);
		disconnect();
		connect();
	}
}
