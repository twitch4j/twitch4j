package kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Model representing a twitch ingest server.
 * <p>
 * The Twitch ingesting system is the first stop for a broadcast stream.
 * An ingest server receives your stream, and the ingesting system
 * authorizes and registers streams, then prepares them for viewers.
 */
@Data
public class KrakenIngest {

	@JsonProperty("_id")
	private Long id;

	private String name;

	private Integer availability;

	@JsonProperty("default")
	private Boolean isDefault;

	private String urlTemplate;
}
