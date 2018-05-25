package twitch4j.api.kraken.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Model representing a chat room.
 */
@Data
public class ChatRoom {

	@JsonProperty("_id")
	private String id;

	@JsonProperty("owner_id")
	private Long ownerId;

	private String name;

	private String topic;

	@JsonProperty("is_previewable")
	private boolean isPreviewable;

	@JsonProperty("minimum_allowed_role")
	private String minimumAllowedRole;
}
