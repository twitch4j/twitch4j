package me.philippheuer.twitch4j.streamlabs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Donation {

	private Long donationId;

	/**
	 * The time gets returned as unix timestamp in seconds
	 */
	@JsonFormat(shape= JsonFormat.Shape.NUMBER, pattern="s")
	private Calendar createdAt;

	private String currency;

	private Double amount;

	private String name;

	private String message;
}
