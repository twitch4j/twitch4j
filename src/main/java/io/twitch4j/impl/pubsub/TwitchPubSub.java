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

package io.twitch4j.impl.pubsub;

import io.twitch4j.impl.utils.SocketImpl;
import io.twitch4j.pubsub.IPubSub;
import io.twitch4j.pubsub.ITopic;
import io.twitch4j.pubsub.PubSubException;
import io.twitch4j.utils.LoggerType;
import io.twitch4j.IClient;
import io.twitch4j.TwitchAPI;
import io.twitch4j.impl.utils.SocketImpl;
import io.twitch4j.pubsub.IPubSub;
import io.twitch4j.pubsub.ITopic;
import io.twitch4j.pubsub.PubSubException;
import io.twitch4j.utils.LoggerType;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

public class TwitchPubSub extends SocketImpl<PubSubListener> implements IPubSub {
	private final Logger logger = LoggerFactory.getLogger(LoggerType.PUBSUB);
	private final WebSocketClient ws = new WebSocketClient();

	public TwitchPubSub(IClient client) {
		super(client, new PubSubListener(client), TwitchAPI.PUBSUB);
	}

	private void sendRawMessage(String message) throws Exception {
		getSession().orElseThrow(() -> new PubSubException("Twitch PubSub is not connected")).getRemote().sendString(message);
	}

	@Override
	public Set<ITopic> getTopics() {
		return getListener().getTopics();
	}

	@Override
	public void registerTopics(Collection<ITopic> topics) {
		topics.forEach(topic -> {
			if (!getTopics().contains(topic)) {
				if (getSession().isPresent()) {
					try {
						sendRawMessage(((TwitchPubSubTopic) topic).listen());
					} catch (Exception e) {
						logger.error(ExceptionUtils.getMessage(e), e);
					}
				}
				getTopics().add(topic);
			}
		});
	}

	@Override
	public void unregisterTopics(Collection<ITopic> topics) {
		topics.forEach(topic -> {
			if (getTopics().contains(topic)) {
				if (getSession().isPresent()) {
					try {
						sendRawMessage(((TwitchPubSubTopic) topic).unlisten());
					} catch (Exception e) {
						logger.error(ExceptionUtils.getMessage(e), e);
					}
				}
				getTopics().remove(topic);
			}
		});
	}
}
