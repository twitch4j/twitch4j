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

package me.philippheuer.twitch4j.impl.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.api.kraken.models.Channel;
import me.philippheuer.twitch4j.auth.ICredential;
import me.philippheuer.twitch4j.pubsub.ITopic;
import me.philippheuer.twitch4j.pubsub.PubSubException;
import me.philippheuer.twitch4j.pubsub.Topic;
import me.philippheuer.twitch4j.utils.LoggerType;
import me.philippheuer.twitch4j.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Getter
public class TwitchPubSubTopic implements ITopic {
	private final Topic topic;
	private final Channel channel;
	private final ICredential credential;
	private final String nonce;

	private IClient client;

	private final Logger logger;

	public TwitchPubSubTopic(Topic topic, Channel channel, ICredential credential) {
		this.topic = topic;
		this.channel = channel;
		this.credential = credential;
		this.nonce = Util.generateNonce(toString());

		this.logger = LoggerFactory.getLogger(String.format("%s @ %s", LoggerType.PUBSUB, topic.name().toLowerCase()));
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof ITopic && o.toString().equals(toString()) && ((ITopic) o).getCredential().equals(credential);
	}

	protected String listen() {
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode node = mapper.createObjectNode();
		node.put("type", "LISTEN");
		if (!StringUtils.isBlank(nonce)) {
			node.put("nonce", nonce);
		}
		node.set("data", createData());

		return node.textValue();
	}

	private ObjectNode createData() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode data = mapper.createObjectNode();
		List<String> topic = Collections.singletonList(toString());
		String accessToken = null;

		if (this.topic.isRequiredScope()) {
			try {
				if (this.topic.getScopes().length > 1) {
					accessToken = credential.getAccessToken();
				} else if (credential.getScopes().contains(this.topic.getScopes()[0])) {
					accessToken = credential.getAccessToken();
				} else throw new PubSubException(String.format("The required scope is not authorized that I need. [Required: %s]", this.topic.getScopes()[0].toString()));
			} catch (PubSubException ex) {
				logger.error(ExceptionUtils.getMessage(ex), ex);
			}
		}
		if (StringUtils.isBlank(accessToken)) {
			data.put("auth_token", accessToken);
		}
		data.putPOJO("topics", topic);

		return data;
	}

	protected String unlisten() {
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode node = mapper.createObjectNode();
		node.put("type", "UNLISTEN");
		if (!StringUtils.isBlank(nonce)) {
			node.put("nonce", nonce);
		}
		node.set("data", createData());

		return node.textValue();
	}

	@Override
	public String toString() {
		String topic;
		switch (this.topic) {
			case CHAT_MODERATION:
				topic = this.topic.parse(credential.getUser().getId(), channel.getId());
				break;
			case VIDEO_PLAYBACK:
				topic = this.topic.parse(channel.getUsername());
				break;
			case WHISPERS: // access token must be linked for user
			case BITS: // access token must be linked for channel (userId = channelId)
				topic = this.topic.parse(credential.getUser().getId());
				break;
			default:
				topic = this.topic.parse(channel.getId());
				break;
		}

		return topic;
	}
}
