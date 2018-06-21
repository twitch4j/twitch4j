package twitch4j.api.helix.model;

import lombok.Data;

@Data
public class Game {
	private Long id;
	private String name;
	private String boxArtUrl;
}
