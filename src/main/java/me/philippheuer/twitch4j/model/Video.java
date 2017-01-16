package me.philippheuer.twitch4j.model;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Video {

	@JsonProperty("_id")
    private long id;

    private String title;

    private String description;

    private String description_html;

    private long broadcastId;

    private String broadcastType; // highlight|archive|upload

    private Channel channel;

    private String status;

    private String tagList; // Possibly used for exporting to YouTube. No real use.

    private String game;

    private int length;

    private Map<String, String> preview;

    private String url;

    private int views;

    private VideoFramerates fps;

    private VideoResolutions resolutions;

    private Date createdAt;

    private Date publishedAt;
}
