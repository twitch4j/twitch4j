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

package me.philippheuer.twitch4j.irc.channel;

import me.philippheuer.twitch4j.api.kraken.models.Channel;
import me.philippheuer.twitch4j.api.kraken.models.User;
import me.philippheuer.twitch4j.irc.DefaultColor;
import me.philippheuer.twitch4j.irc.chat.IChatRoom;
import me.philippheuer.twitch4j.irc.exceptions.ModerationException;

import java.awt.*;
import java.util.List;

public interface IChannel {
	Channel getChannelInfo();
	IModeration getModeration() throws ModerationException;
	List<User> getModerators();
	List<User> getSubscribers();
	List<User> getChatUsers();
	boolean isJoined();
	boolean isBotModerator();
	boolean isBotEditor();
	void sendMessage(String message);
	void sendActionMessage(String message);
	void part();
	void join();
	void changeColorNames(DefaultColor color);
	void changeColorNames(Color color);
	void changeColorNames(String hexColor);
	// TODO: Supporting Chat Rooms
	List<IChatRoom> getChannelChatRooms();
}
