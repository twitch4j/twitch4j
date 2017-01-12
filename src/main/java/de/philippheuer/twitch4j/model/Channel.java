package de.philippheuer.twitch4j.model;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import de.philippheuer.twitch4j.auth.TwitchCredential;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import lombok.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {
	
	@JsonProperty("_id")
    private Long id;
	
	private String broadcasterLanguage;
	
	private Date createdAt;
	
	private String displayName;
	
	private String email;
	
	private int followers;
	
	private String game;
	
	private String language;
	
	private String logo;
	
	private boolean mature;
	
	private String name;
	
	private boolean partner;
	
	private String profileBanner;
	
    private String profileBannerBackgroundColor;
	
    private String status;
    
    private String streamKey;
    
    private Date updatedAt;
    
    private String url;
    
    private String videoBanner;
    
    private long views;
    
    // Holds related Credentials
    private Optional<TwitchCredential> twitchCredential;
}
