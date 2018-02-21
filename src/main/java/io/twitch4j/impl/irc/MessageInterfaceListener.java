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

@Getter
public class MessageInterfaceListener extends TwitchListener {
	private final Logger logger = LoggerFactory.getLogger(LoggerType.TMI);
	public MessageInterfaceListener(IClient client) {
		super(client);
	}
	private final Map<String, IChannel> channels = new LinkedHashMap<>();

	public void addChannel(String channelName) {
		IChannel channel = new ChannelEndpoint(this, channelName);
		if (isConnected()) {
			channel.join();
		}
		channels.put(channelName, channel);
	}

	public void removeChannel(String channelName) {
		IChannel channel = channels.remove(channelName);
		if (isConnected()) {
			channel.part();
		}
	}

	public IChannel getChannel(String channel) {
		return channels.get(channel);
	}

	@Override
	public void onWebSocketText(String raw) {
		IrcMessage message = IrcMessage.parseMessage(raw);
		logger.debug(message.toString());
		// TODO: handling message
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
			if (!channels.containsKey(getClient().getConfiguration().getBot().getUsername())) {
				addChannel(getClient().getConfiguration().getBot().getUsername());
			}
			channels.forEach((name, channel) -> channel.join());
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
