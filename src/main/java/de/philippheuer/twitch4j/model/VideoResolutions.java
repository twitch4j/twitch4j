package de.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoResolutions {
	
	private String medium;
	
    private String mobile;
    
    private String high;
    
    private String low;
    
    private String chunked;
}
