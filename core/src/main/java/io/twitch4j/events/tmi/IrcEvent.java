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

package io.twitch4j.events.tmi;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.ITwitchClient;
import io.twitch4j.events.Event;
import io.twitch4j.impl.events.tmi.IrcEventBuilder;
import io.twitch4j.tmi.model.IrcCommand;
import io.twitch4j.tmi.model.IrcMessage;
import io.vertx.core.json.JsonObject;

@JsonDeserialize(builder = IrcEventBuilder.class)
public abstract class IrcEvent extends Event {
    public static IrcEvent from(ITwitchClient client, IrcMessage message) {
        return new IrcEventBuilder()
                .client(client)
                .command(message.getCommand())
                .parameters(message.getParameters())
                .message(message.getMessage())
                .hostmask(message.getHostmask())
                .tags(message.getTags())
                .build();
    }

    public abstract IrcCommand getCommand();

    public abstract String getParameters();

    public abstract String getMessage();

    public abstract String getHostmask();

    public abstract JsonObject getTags();
}
