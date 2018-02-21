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

import io.twitch4j.api.kraken.models.User;
import io.twitch4j.irc.IUser;
import io.twitch4j.irc.channel.IChannel;
import io.twitch4j.irc.exceptions.UserNotFoundException;
import lombok.Getter;
import io.twitch4j.api.kraken.models.User;
import io.twitch4j.impl.irc.channel.ChannelEndpoint;
import io.twitch4j.irc.IUser;
import io.twitch4j.irc.channel.IChannel;
import io.twitch4j.irc.exceptions.UserNotFoundException;

@Getter
public class UserEndpoint implements IUser {
	private final MessageInterfaceListener listener;
	private final String userName;
	private final IChannel channel;

	public UserEndpoint(MessageInterfaceListener listener, String userName) {
		this.listener = listener;
		this.userName = userName;
		this.channel = new ChannelEndpoint(listener, userName);
	}

	@Override
	public User getUserInfo() {
		return listener.getClient().getKrakenApi().userOperation().getByName(userName).orElseThrow(() -> new UserNotFoundException(channel + " is not exists."));
	}

	@Override
	public void sendMessage(String message) {
		listener.getBotChannel().sendMessage(String.format("/w %s %s", userName, message));
	}
}
