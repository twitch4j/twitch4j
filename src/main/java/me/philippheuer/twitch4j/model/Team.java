package me.philippheuer.twitch4j.model;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
	
	@JsonProperty("_id")
    private long id;
	
	private String name;
    
	private String displayName;
    
	private String info;
	
	private String logo;
    
	private String background;
	
	private String banner;
    
    private Date createdAt;
    
    private Date updatedAt;
}
