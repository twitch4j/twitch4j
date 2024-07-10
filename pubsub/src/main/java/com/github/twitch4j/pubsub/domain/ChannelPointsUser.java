package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

@Data
@Setter(onMethod_ = { @Deprecated })
public class ChannelPointsUser {

	private String id;
	private String login;
	private String displayName;

}
