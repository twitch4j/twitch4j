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

package io.twitch4j.tmi;

import io.twitch4j.ISocket;
import io.twitch4j.tmi.channel.IChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

public interface IMessageInterface extends ISocket {
    void addChannel(String channelName);

    default void addChannels(Collection<String> channels) {
        channels.forEach(this::addChannel);
    }

    default void addChannels(String... channels) {
        addChannels(Arrays.asList(channels));
    }

    void removeChannel(String channelName);

    default void removeChannels(Collection<String> channels) {
        channels.forEach(this::removeChannel);
    }

    default void removeChannels(String... channels) {
        removeChannels(Arrays.asList(channels));
    }

    IChannel getChannel(String channelName);

    IChannel getBotChannel();

    IUser createPrivateMessage(String userName);

    boolean isJoined(String channelName);

    void changeColorNames(DefaultColor color);

    void changeColorNames(Color color);

    void changeColorNames(String hexColor);
}
