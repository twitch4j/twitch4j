package me.philippheuer.twitch4j.streamlabs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import me.philippheuer.twitch4j.model.User;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Donation {

	private Long donationId;

    private Date createdAt;

    private String currency;

    private Double amount;

    private String name;

    private String message;
}
