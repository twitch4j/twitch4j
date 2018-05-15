package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Model representing a chat room.
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
