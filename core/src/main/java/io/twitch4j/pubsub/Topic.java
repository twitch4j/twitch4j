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

package io.twitch4j.pubsub;

import io.twitch4j.auth.Scope;
import lombok.Getter;

@Getter
public enum Topic {
	CHANNEL_SUBSCRIPTION("channel-subscribe-events-v1.%d", Scope.CHANNEL_SUBSCRIPTIONS) {
		@Override
		public String parse(Object... data) {
			long channelId = Long.parseLong(String.valueOf(data[0]));
			return String.format(getTopic(), channelId);
		}
	},
	CHAT_MODERATION("chat_moderator_actions.%d.%d", Scope.CHAT_LOGIN) {
		@Override
		public String parse(Object... data) {
			long userId = Long.parseLong(String.valueOf(data[0]));
			long channelId = Long.parseLong(String.valueOf(data[1]));
			return String.format(getTopic(), userId, channelId);
		}
	},
	VIDEO_PLAYBACK("video-playback.%s", false) {
		@Override
		public String parse(Object... data) {
			String channelName = String.valueOf(data[0]);
			return String.format(getTopic(), channelName);
		}
	},
	WHISPERS("whispers.%d", Scope.CHAT_LOGIN) {
		@Override
		public String parse(Object... data) {
			long userId = Long.parseLong(String.valueOf(data[0]));
			return String.format(getTopic(), userId);
		}
	},
	BITS("channel-bits-events-v1.%d", true) {
		@Override
		public String parse(Object... data) {
			long channelId = Long.parseLong(String.valueOf(data[0]));
			return String.format(getTopic(), channelId);
		}
	};

	private final String topic;
	private final Scope[] scopes;
	private final boolean requiredScope;

	Topic(String topic, Scope scope) {
		this.topic = topic;
		this.scopes = new Scope[] { scope };
		this.requiredScope = true;
	}

	Topic(String topic, boolean requiredScope) {
		this.topic = topic;
		this.requiredScope = requiredScope;
		this.scopes = (requiredScope) ? Scope.values() : null;
	}

	public abstract String parse(Object... data);
}
