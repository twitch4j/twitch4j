package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import me.philippheuer.twitch4j.enums.SubPlan;

import java.util.Date;

/**
 * Model representing a subscription.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscription {

	@JsonProperty("_id")
	private String id;

	private Date createdAt;

	private User user;

	@JsonProperty("sub_plan")
	private String sub_plan_code;

	@JsonProperty("sub_plan_name")
	private String sub_plan_name;

	@JsonIgnore
	private String message;

	@JsonIgnore
	private Integer streak;

	/**
	 * Holds the subscription plan
	 */
	@JsonIgnore
	private SubPlan subPlan;

	/**
	 * Sets the subplan using the string
	 * @param code sub plan by code
	 */
	public void setSubPlanByCode(String code) {
		if(code.equals("Prime")) {
			setSubPlan(SubPlan.PRIME);
		} else if(code.equals("1000")) {
			setSubPlan(SubPlan.TIER_1);
		} else if(code.equals("2000")) {
			setSubPlan(SubPlan.TIER_2);
		} else if(code.equals("3000")) {
			setSubPlan(SubPlan.TIER_3);
		} else {
			setSubPlan(SubPlan.UNKNOWN);
		}
	}
}
