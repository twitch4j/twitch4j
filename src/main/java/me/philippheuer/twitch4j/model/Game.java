package me.philippheuer.twitch4j.model;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {
	
	@JsonProperty("_id")
    private Long id;
    
	private String name;
    
    private GameBox box;
    
    private GameLogo logo;
    
    private long giantbombId;
    
    private int popularity; // From search results
    
    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class GameBox {
    	private String large;
        private String medium;
        private String small;
        private String template;
    }
    
    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class GameLogo {
    	private String large;
        private String medium;
        private String small;
        private String template;
    }
}