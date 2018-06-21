package twitch4j.api.kraken.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;

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
	private Long id;

	private String name;

	private String displayName;

	private String logo;

	private String type;

	private String bio;

	private Date updatedAt;

	private Date createdAt;

}
