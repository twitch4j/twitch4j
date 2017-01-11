package de.philippheuer.twitch4j.model;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscription {
	
	@JsonProperty("_id")
	private Long id;
	
    private Date createdAt;
    
    private Integer streak;
	
    private String message;
    
    private Boolean isPrimeSub;
    
	private User user;
}