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

package io.twitch4j.tmi.channel;

import io.twitch4j.tmi.exceptions.ModerationException;

import java.util.concurrent.TimeUnit;

public interface IModeration {
    void banUser(String user, String reason);

    default void banUser(String user) {
        banUser(user, null);
    }

    void timeoutUser(String user, int seconds, String reason);

    default void timeoutUser(String user, int seconds) {
        timeoutUser(user, seconds, null);
    }

    void unbanUser(String user);

    default void untimeoutUser(String user) {
        unbanUser(user);
    }

    void emoteOnly();

    void emoteOnlyOff();

    void r9k();

    void r9kOff();

    void slowmode(final int seconds);

    void slowmodeOff();

    void clearChat();

    default void followersOnly(int time, TimeUnit timeUnit) {
        followersOnly(TimeUnit.SECONDS.convert(time, timeUnit));
    }

    default void followersOnly() {
        followersOnly(0);
    }

    void followersOnly(long seconds);

    void followersOff();

    IEditor getEditor() throws ModerationException;
}
