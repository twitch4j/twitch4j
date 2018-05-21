package twitch4j.api.kraken.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

/**
 * Model representing a user.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class User {
	@JsonProperty("_id")
	private long id;
	private String name;
	private String displayName;
	private String logo;
	private String type;
	private String bio;
	private Instant updatedAt;
	private Instant createdAt;

}
