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

import io.twitch4j.event.Event;
import io.twitch4j.irc.channel.IRoomState;
import io.twitch4j.irc.event.RoomStateChangedEvent;
import io.twitch4j.irc.event.RoomStateEvent;
import lombok.Data;

import java.util.Locale;

@Data
public class RoomState implements IRoomState {
	private Locale broadcastLanguage = null;
	private boolean robot9000 = false;
	private long slowTime = 0;
	private boolean subscribersOnly = false;

	public RoomState(ChannelEndpoint channel) {
		channel.getListener().getClient()
				.getDispatcher().registerListener((Event event) -> {
					if (event instanceof RoomStateEvent) {
						RoomState.this.broadcastLanguage = ((RoomStateEvent) event).getBroadcastLang();
						RoomState.this.robot9000 = ((RoomStateEvent) event).isR9k();
						RoomState.this.slowTime = ((RoomStateEvent) event).getSlow();
						RoomState.this.subscribersOnly = ((RoomStateEvent) event).isSubscribers();
					} else if (event instanceof RoomStateChangedEvent) {
						switch (((RoomStateChangedEvent) event).getKey()) {
							case "broadcaster_lang":
								if (((RoomStateChangedEvent) event).getValue() != null) {
									RoomState.this.broadcastLanguage = Locale.forLanguageTag(((RoomStateChangedEvent) event).getValue());
								} else {
									RoomState.this.broadcastLanguage = null;
								}
								break;
							case "r9k":
								RoomState.this.robot9000 = ((RoomStateChangedEvent) event).getValue().equals("1");
								break;
							case "slow":
								RoomState.this.slowTime = Long.parseLong(((RoomStateChangedEvent) event).getValue());
								break;
							case "subs_only":
								RoomState.this.subscribersOnly = ((RoomStateChangedEvent) event).getValue().equals("1");
								break;
						}
					}
		});
	}

	@Override
	public boolean isSlowMode() {
		return slowTime > 0;
	}
}
