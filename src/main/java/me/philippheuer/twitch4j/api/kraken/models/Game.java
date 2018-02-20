package me.philippheuer.twitch4j.api.kraken.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Game extends KrakenModel {
	private Long id;
	private String name;
	private String thumbnailUri;
}
