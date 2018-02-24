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

package io.twitch4j.impl.irc;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.twitch4j.impl.utils.TwitchListener;
import io.twitch4j.irc.channel.IChannel;
import io.twitch4j.irc.model.IrcMessage;
import io.twitch4j.utils.LoggerType;
import lombok.Getter;
import io.twitch4j.IClient;
import io.twitch4j.impl.irc.channel.ChannelEndpoint;
import io.twitch4j.impl.utils.TwitchListener;
import io.twitch4j.irc.channel.IChannel;
import io.twitch4j.irc.model.IrcMessage;
import io.twitch4j.utils.LoggerType;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

@Getter
public class MessageInterfaceListener extends TwitchListener {
	private final Logger logger = LoggerFactory.getLogger(LoggerType.TMI);
	public MessageInterfaceListener(IClient client) {
		super(client);
	}
	private final Map<String, IChannel> channels = new LinkedHashMap<>();
	private final ConcurrentHashMap<String, IChannel> channelQueue = new ConcurrentHashMap<>();

	public void addChannel(String channelName) {
		IChannel channel = new ChannelEndpoint(this, channelName);
		if (isConnected()) {
			channel.join();
			channels.put(channelName, channel);
		} else channelQueue.put(channelName, channel);
	}

	public void removeChannel(String channelName) {
		if (isConnected()) {
			channels.remove(channelName).part();
		} else channelQueue.remove(channelName);
	}

	public IChannel getChannel(String channel) {
		if (isConnected()) {
			return channels.get(channel);
		} else return channelQueue.get(channel);
	}

	@Override
	public void onWebSocketText(String raw) {
		IrcMessage message = IrcMessage.parseMessage(raw);
		switch (message.getCommand()) {
			case PING:
				sendPong();
			default:
				IrcMessage.parseAndDispatchEvent(message, getClient());
		}
	}

	private void sendPong() {
		getSession().ifPresent(session -> {
			try {
				session.getRemote().sendString("PONG :tmi.twitch.tv");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
	}

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		try {
			((TwitchMessageInterface) getClient().getMessageInterface())
					.initialize(session);
		} catch (IOException e) {
			logger.error("Connection error to Message Interface:", e);
		}
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		logger.error("Exception on Message Interface:", cause);
	}

	public IChannel getBotChannel() {
		return getChannel(getClient().getConfiguration().getBot().getUsername());
	}
}
