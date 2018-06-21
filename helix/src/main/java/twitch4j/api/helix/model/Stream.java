package twitch4j.api.helix.model;

import twitch4j.common.enums.StreamType;

import java.time.Instant;
import java.util.Locale;
import java.util.Set;

public class Stream {
	private Long id;
	private Long userId;
	private Long gameId;
	private Set<String> communityIds;
	private StreamType type;
	private String title;
	private Integer viewerCount;
	private Instant startedAt;
	private Locale language;
	private String thumbnailUrl;
}
