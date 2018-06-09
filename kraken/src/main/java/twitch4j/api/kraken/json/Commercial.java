package twitch4j.api.kraken.json;

import lombok.Data;

@Data
public class Commercial {
	private Integer length;
	private String message;
	private Integer retryAfter;
}
