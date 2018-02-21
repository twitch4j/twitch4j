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

import io.twitch4j.impl.irc.channel.ChannelEndpoint;
import io.twitch4j.impl.utils.SocketImpl;
import io.twitch4j.irc.DefaultColor;
import io.twitch4j.irc.IMessageInterface;
import io.twitch4j.irc.IUser;
import io.twitch4j.irc.channel.IChannel;
import io.twitch4j.IClient;
import io.twitch4j.TwitchAPI;
import io.twitch4j.impl.irc.channel.ChannelEndpoint;
import io.twitch4j.impl.utils.SocketImpl;
import io.twitch4j.irc.DefaultColor;
import io.twitch4j.irc.IMessageInterface;
import io.twitch4j.irc.IUser;
import io.twitch4j.irc.channel.IChannel;
import org.eclipse.jetty.websocket.api.Session;

import java.awt.*;
import java.io.IOException;

public class TwitchMessageInterface extends SocketImpl<MessageInterfaceListener> implements IMessageInterface {

	public TwitchMessageInterface(IClient client) {
		super(client, new MessageInterfaceListener(client), TwitchAPI.IRCWS);
	}

	public IChannel getBotChannel() {
		return getListener().getChannels().get(getClient().getConfiguration().getBot().getUsername());
	}

	@Override
	public void addChannel(String channelName) {
		getListener().addChannel(channelName);
	}

	@Override
	public void removeChannel(String channelName) {
		getListener().removeChannel(channelName);
	}

	@Override
	public IChannel getChannel(String channelName) {
		if (getListener().getChannels().containsKey(channelName))
			return getListener().getChannels().get(channelName);
		else return new ChannelEndpoint(getListener(), channelName);
	}

	@Override
	public IUser createPrivateMessage(String userName) {
		return new UserEndpoint(getListener(), userName);
	}

	@Override
	public void changeColorNames(DefaultColor color) {
		getBotChannel().changeColorNames(color);
	}

	@Override
	public void changeColorNames(Color color) {
		getBotChannel().changeColorNames(color);
	}

	@Override
	public void changeColorNames(String hexColor) {
		getBotChannel().changeColorNames(hexColor);
	}

	protected void initialize(Session session) throws IOException {
		session.getRemote().sendString("CAP REQ :twitch.tv/membership");
		session.getRemote().sendString("CAP REQ :twitch.tv/tags");
		session.getRemote().sendString("CAP REQ :twitch.tv/commands");

		session.getRemote().sendString("CAP REQ :twitch.tv/commands");
		session.getRemote().sendString("PASS " + getClient().getConfiguration().getBot().getPassword());
		session.getRemote().sendString("NICK " + getClient().getConfiguration().getBot().getUsername().toLowerCase());
	}
}
