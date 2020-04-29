package com.github.twitch4j.tmi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Host {
	
	@JsonProperty("host_id")
	private String hostId;
	
	@JsonProperty("host_login")
	private String hostLogin;
	
	@JsonProperty("target_id")
	private String targetId;
	
	@JsonProperty("target_login")
	private String targetLogin;
	
}
