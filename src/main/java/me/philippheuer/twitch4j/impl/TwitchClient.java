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

package me.philippheuer.twitch4j.impl;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.IConfiguration;
import me.philippheuer.twitch4j.api.helix.IHelix;
import me.philippheuer.twitch4j.api.kraken.IKraken;
import me.philippheuer.twitch4j.auth.IManager;
import me.philippheuer.twitch4j.event.IDispatcher;
import me.philippheuer.twitch4j.impl.api.helix.HelixApi;
import me.philippheuer.twitch4j.impl.api.kraken.KrakenApi;
import me.philippheuer.twitch4j.impl.auth.Credential;
import me.philippheuer.twitch4j.impl.auth.CredentialManager;
import me.philippheuer.twitch4j.impl.event.EventDispatcher;
import me.philippheuer.twitch4j.impl.irc.TwitchMessageInterface;
import me.philippheuer.twitch4j.impl.pubsub.TwitchPubSub;
import me.philippheuer.twitch4j.irc.IMessageInterface;
import me.philippheuer.twitch4j.pubsub.IPubSub;
import me.philippheuer.twitch4j.utils.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class TwitchClient implements IClient {
	private final IConfiguration configuration;
	private final Logger logger = LoggerFactory.getLogger(LoggerType.CORE);
	private final IManager credentialManager = new CredentialManager(this);
	private final IMessageInterface messageInterface = new TwitchMessageInterface(this);
	private final IPubSub pubSub = new TwitchPubSub(this);
	private final IDispatcher dispatcher = new EventDispatcher(this);

	public TwitchClient(IConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public boolean isConnected() {
		return messageInterface.isConnected() || pubSub.isConnected();
	}

	@Override
	public void connect() throws Exception {
		messageInterface.connect();
		pubSub.connect();
	}

	@Override
	public void disconnect() throws Exception {
		messageInterface.disconnect();
		pubSub.connect();
	}

	@Override
	public void reconnect() throws Exception {
		messageInterface.reconnect();
		pubSub.reconnect();
	}

	@Override
	public IHelix getHelixApi() {
		return new HelixApi(this);
	}

	@Override
	public IKraken getKrakenApi() {
		return new KrakenApi(this);
	}

	public static class BotCredential extends Credential implements IConfiguration.IBot {
		public BotCredential(String accessToken, String refreshToken) {
			super(accessToken, refreshToken);
		}
	}
}
