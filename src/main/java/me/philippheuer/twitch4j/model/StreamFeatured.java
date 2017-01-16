package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamFeatured {

	private String text;

    private String image;

    private boolean sponsored;

    private int priority;

    private boolean scheduled;

    private Stream stream;

}
