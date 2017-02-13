package me.philippheuer.twitch4j.model.tmi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chatter {

	private List<String> moderators;

	private List<String> staff;

	private List<String> admins;

	private List<String> globalMods;

	private List<String> viewers;
}
