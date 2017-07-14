package me.philippheuer.twitch4j.streamlabs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import me.philippheuer.util.conversion.UnixTimestampDeserializer;

import java.util.Calendar;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Donation {

	private Long donationId;

	/**
	 * The time gets returned as unix timestamp in seconds
	 */
	@JsonDeserialize(using = UnixTimestampDeserializer.class)
	private Calendar createdAt;

	private String currency;

	private Double amount;

	private String name;

	private String message;
}
