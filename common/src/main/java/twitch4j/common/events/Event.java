package twitch4j.common.events;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Event {
	private Instant createdAt = Instant.now();
	private String eventId = UUID.randomUUID().toString();
}
