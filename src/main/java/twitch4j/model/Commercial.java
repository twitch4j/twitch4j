package twitch4j.model;

import lombok.Data;

@Data
public class Commercial {
	private Integer length;
	private String message;
	private Integer retryAfter;
}
