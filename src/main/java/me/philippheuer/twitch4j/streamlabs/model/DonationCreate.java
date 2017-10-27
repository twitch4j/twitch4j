package me.philippheuer.twitch4j.streamlabs.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DonationCreate {
	/**
	 * ID of the newly created donation
	 */
    private Long donationId;

}
