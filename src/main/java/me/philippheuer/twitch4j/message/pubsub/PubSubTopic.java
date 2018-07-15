package me.philippheuer.twitch4j.message.pubsub;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PubSubTopic {
	private final List<String> topics;
	private final String authToken;

	@Setter
	@NoArgsConstructor
	@Accessors(fluent = true, chain = true)
	public static class Builder {
		private PubSubType type;
		private String subject;
		private String accessToken;

		public PubSubTopic build() {
			return new PubSubTopic(Collections.singletonList(type.subject(subject)), accessToken);
		}
	}
}
