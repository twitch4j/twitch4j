package twitch4j.api.helix.json;

import lombok.Data;

import java.time.Instant;

@Data
public class Follow {
	private Long fromId;
	private Long toId;
	private Instant followedAt;
}
