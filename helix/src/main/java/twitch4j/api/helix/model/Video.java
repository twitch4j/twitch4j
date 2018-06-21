package twitch4j.api.helix.model;

import lombok.Data;
import twitch4j.common.enums.VideoAccess;
import twitch4j.common.enums.VideoType;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

@Data
public class Video {
	private Long id;
	private Long userId;
	private String title;
	private String description;
	private Instant createdAt;
	private Instant publishedAt;
	private String url;
	private String thumbnailUrl;
	private VideoAccess viewable;
	private Integer viewCount;
	private Locale language;
	private VideoType type;
	private Duration duration;
}
