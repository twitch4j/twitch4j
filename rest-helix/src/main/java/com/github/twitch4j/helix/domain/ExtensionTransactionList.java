package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtensionTransactionList {
	
	/**
     * Array of requested transactions.
	 */
	@JsonProperty("data")
	private List<ExtensionTransaction> transactions;
	
	/**
     * If provided, is the key used to fetch the next page of data. If not provided, the current response is the last page of data available.
	 */
	@JsonProperty("pagination")
	private String pagination;
	
}
