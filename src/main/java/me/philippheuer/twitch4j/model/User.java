package me.philippheuer.twitch4j.model;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	@JsonProperty("_id")
    private Long id;

	private String name;

    private String displayName;

    private String logo;

    private String type;

    private String bio;

    private Date updatedAt;

    private Date createdAt;

}
