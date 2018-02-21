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

package io.twitch4j.impl.irc.channel;

import io.twitch4j.api.kraken.models.Channel;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.impl.irc.MessageInterfaceListener;
import io.twitch4j.irc.DefaultColor;
import io.twitch4j.irc.channel.IChannel;
import io.twitch4j.irc.channel.IModeration;
import io.twitch4j.irc.chat.IChatRoom;
import io.twitch4j.irc.exceptions.ChannelNotFoundException;
import io.twitch4j.irc.exceptions.ModerationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.twitch4j.api.kraken.models.Channel;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.impl.irc.MessageInterfaceListener;
import io.twitch4j.irc.DefaultColor;
import io.twitch4j.irc.channel.IChannel;
import io.twitch4j.irc.channel.IModeration;
import io.twitch4j.irc.chat.IChatRoom;
import io.twitch4j.irc.exceptions.ChannelNotFoundException;
import io.twitch4j.irc.exceptions.ModerationException;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ChannelEndpoint implements IChannel {
	private final MessageInterfaceListener listener;
	private final String channel;

	@Override
	public Channel getChannelInfo() {
		return listener.getClient().getKrakenApi().channelOperation().getByName(channel).orElseThrow(() -> new ChannelNotFoundException(channel + " is not exists."));
	}

	@Override
	public IModeration getModeration() throws ModerationException {
		if (isBotModerator()) {
			return new Moderation(this);
		} else throw new ModerationException("Bot is not moderator on channel: " + channel);
	}

	@Override
	public List<User> getModerators() {
		return null;
	}

	@Override
	public List<User> getSubscribers() {
		return null;
	}

	@Override
	public List<User> getChatUsers() {
		return null;
	}

	@Override
	public boolean isJoined() {
		return getListener().getChannels().containsKey(channel);
	}

	@Override
	public boolean isBotModerator() {
		return false;
	}

	@Override
	public boolean isBotEditor() {
		return false;
	}

	@Override
	public void sendMessage(String message) {
		getListener().getSession().ifPresent(session -> {
			try {
				session.getRemote().sendString(String.format("PRIVSMG #%s %s", channel, message));
			} catch (IOException e) {
				getListener().getLogger().error("Cannot send message to channel: " + channel, e);
			}
		});
	}

	@Override
	public void sendActionMessage(String message) {
		sendMessage("/me " + message);
	}

	@Override
	public void part() {
		getListener().getSession().ifPresent(session -> {
			if (isJoined()) {
				try {
					session.getRemote().sendString("PART #" + channel);
					getListener().getChannels().remove(channel);
				} catch (IOException e) {
					getListener().getLogger().error("Cannot left to channel: " + channel, e);
				}
			}
		});
	}

	@Override
	public void join() {
		getListener().getSession().ifPresent(session -> {
			if (!getListener().getChannels().containsKey(channel)) {
				try {
					session.getRemote().sendString("JOIN #" + channel);
					getListener().getChannels().put(channel, this);
				} catch (IOException e) {
					getListener().getLogger().error("Cannot join to channel: " + channel, e);
				}
			}
		});
	}

	@Override
	public void changeColorNames(DefaultColor color) {
		sendMessage("/color " + color.name().toLowerCase());
	}

	@Override
	public void changeColorNames(Color color) {
		changeColorNames("#" + Integer.toHexString(color.getRGB()).substring(2));
	}

	@Override
	public void changeColorNames(String hexColor) {
		throw new UnsupportedOperationException("Temporary unsupported!");
	}

	@Override
	public List<IChatRoom> getChannelChatRooms() {
		throw new UnsupportedOperationException("Chat Rooms is currently temporary unsupported!");
	}
}
