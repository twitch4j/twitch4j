package twitch4j.api.helix.json;

import lombok.Data;

import java.time.Instant;
import java.util.Locale;

@Data
public class Clip {
	private String id;
	private String url;
	private Long broadcasterId;
	private Long creatorId;
	private Long videoId;
	private Long gameId;
	private Locale language;
	private String title;
	private Integer viewCount;
	private Instant createdAt;
	private String thumbnailUrl;
}
