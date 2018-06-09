package twitch4j.pubsub.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import twitch4j.pubsub.PubSubTopic;

@Getter
@RequiredArgsConstructor
public class PubSubListeningException extends RuntimeException {
	private final String message;
	private final PubSubTopic topic;

	@Override
	public String toString() {
		return message + " - " + topic.getTopics().get(0);
	}
}
