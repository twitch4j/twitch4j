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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.twitch4j.ITwitchClient;
import io.twitch4j.impl.utils.TwitchListener;
import io.twitch4j.pubsub.ITopic;
import io.twitch4j.pubsub.PubSubException;
import io.twitch4j.pubsub.event.PubSubBitsEvent;
import io.twitch4j.enums.TwitchComponents;
import lombok.Getter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PubSubListener extends TwitchListener {
	private final Logger logger = LoggerFactory.getLogger(TwitchComponents.PUBSUB);

	@Getter
	private final Set<ITopic> topics = new LinkedHashSet<ITopic>();

	public PubSubListener(ITwitchClient client) {
		super(client);
	}

	@Override
	public void onWebSocketText(String message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = mapper.readValue(message, ObjectNode.class);
			if (node.has("type")) {
				switch (node.get("type").asText()) {
					case "PONG":
						// TODO: Dispatch PONG Response
						break;
					case "PING":
						// TODO: Dispatch PING Response
						break;
					case "RECONNECT":
						getClient().getPubSub().reconnect();
						break;
					case "RESPONSE":
						buildResponse(node);
						break;
					case "MESSAGE":
						buildMessage((ObjectNode) node.get("data"));
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getMessage(e), e);
		}
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		if (StatusCode.isTransmittable(statusCode)) {
			logger.debug(String.format("Close Status Code: %d; Reason: %s", statusCode, reason));
			switch (statusCode) {
				case StatusCode.SHUTDOWN:
				case StatusCode.NORMAL:
					break;
				default:
					throw new PubSubException(String.format("Exception on closing WebSocket [%d]: %s", statusCode, reason));
			}
		}
	}

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
	}

	@Override
	public void onWebSocketError(Throwable cause) {

	}

	private void buildMessage(ObjectNode node) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new LinkedHashMap<>();
		Optional<ITopic> topic = topics.stream()
				.filter(t-> t.toString().equals(node.get("topic").asText()))
				.findFirst();
		ObjectNode rawData = mapper.convertValue(StringEscapeUtils.unescapeJson(node.get("message").textValue()), ObjectNode.class);


		if (topic.isPresent()) {
			switch (topic.get().getTopic()) {
				case BITS:
					data.put("message", mapper.convertValue(rawData.get("data") , PubSubBitsEvent.class));
					break;
			}
		}
	}

	private void buildResponse(ObjectNode node) throws PubSubException {
		Optional<ITopic> topic = topics.stream()
				.filter(t -> ((TwitchPubSubTopic) t).getNonce().equals(node.get("nonce").asText()))
				.findFirst();

		if (topic.isPresent()) {
			if (!StringUtils.isBlank(node.get("error").asText())) {
				StringBuilder sb = new StringBuilder("Cannot request topic ")
						.append("[").append(topic.get().toString()).append("]: ");
				switch (node.get("error").asText()) {
					case "ERR_BADMESSAGE":
						sb.append("Bad Message");
						break;
					case "ERR_BADAUTH":
						sb.append("Bad Authorization");
						break;
					case "ERR_SERVER":
						sb.append("Unknown Server Exception");
						break;
					case "ERR_BADTOPIC":
						sb.append("Bad Topic");
						break;
					default:
						sb.append(node.get("error").asText());
						break;
				}
				throw new PubSubException(sb.toString());
			} else {
				// TODO Dispatch successful listening topic
			}
		} else throw new PubSubException("Received response for unknown topic!");
	}
}
