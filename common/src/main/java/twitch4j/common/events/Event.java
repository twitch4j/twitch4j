package twitch4j.common.events;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public abstract class Event<T extends Event<T>> {
	private Instant createdAt = Instant.now();
	private String eventId = UUID.randomUUID().toString();
}
