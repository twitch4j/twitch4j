package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtensionTransaction {
	
	/**
	 * Unique identifier of the Bits in Extensions Transaction.
	 */
	private String id;
	
	/**
     * UTC timestamp when this transaction occurred.
	 */
	private String timestamp;
	
	/**
     * Twitch User ID of the channel the transaction occurred on.
	 */
	private String broadcasterId;
	
	/**
     * Twitch Display Name of the broadcaster.
	 */
	private String broadcasterName;
	
	/**
     * Twitch User ID of the user who generated the transaction.
	 */
	private String userId;
	
	/**
     * Twitch Display Name of the user who generated the transaction.
	 */
	private String userName;
	
	/**
     * Enum of the product type. Currently only BITS_IN_EXTENSION.
	 */
	private String productType;
	
	/**
	 * Known "product_type" enum values.
	 */
	public static class ProductType {
		// "Currently" only valid product type
		public static String BITS_IN_EXTENSION = "BITS_IN_EXTENSION";
	}
	
	/**
     * JSON Object representing the product acquired, as it looked at the time of the transaction.
	 */
	private ProductData productData;
	
	@Data
	@Setter(AccessLevel.PRIVATE)
	@NoArgsConstructor
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ProductData {
		
		/**
         * Unique identifier for the product across the extension.
		 */
		private String sku;
		
		/**
         * JSON Object representing the cost to acquire the product.
		 */
		private Cost cost;
		
		/**
         * Display Name of the product.
		 */
		@JsonProperty("displayName")
		private String displayName;
		
		/**
         * Flag used to indicate if the product is in development. Either true or false.
		 */
		@JsonProperty("inDevelopment")
		private Boolean inDevelopment;
		
		@Data
		@Setter(AccessLevel.PRIVATE)
		@NoArgsConstructor
		@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Cost {
			
			/**
			 * Number of Bits required to acquire the product.
			 */
			private Integer amount;
			
			/**
			 * Always the string “Bits”.
			 */
			private String type;
			
			/**
			 * Known "type" enum values
			 */
			public static class CostType {
				// "Currently" only valid cost type.
				public static final String BITS = "bits";
			}
		}
		
		
	}
	
}
