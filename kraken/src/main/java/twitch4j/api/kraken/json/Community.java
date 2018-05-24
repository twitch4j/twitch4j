package twitch4j.api.kraken.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Model representing a twitch community.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Community {

	@JsonProperty("_id")
	private String id;

	private String name;

	private String summary;

	private String description;

	private String descriptionHtml;

	private String rules;

	private String rulesHtml;

	private Long ownerId;

	private String avatarImageUrl;

	private String coverImageUrl;

}
