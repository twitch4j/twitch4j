package twitch4j.pubsub.json;

import lombok.Data;

@Data
public class PubSubMessage {
	private String topic;
	private String message;
}
