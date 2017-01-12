package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoFramerates {
	
	private double audioOnly;
	
    private double medium;
    
    private double mobile;
    
    private double high;
    
    private double low;
    
    private double chunked;
}
