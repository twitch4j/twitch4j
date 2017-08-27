package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.enums.SubPlan;
import me.philippheuer.twitch4j.message.irc.IRCParser;
import me.philippheuer.twitch4j.message.irc.IRCTags;

@Getter
public class SubscribeEvent {

	private final String channel;
	private final String username;
	private final int months;
	private final String message;
	private final IRCTags tags;
	private final SubPlan plan;
	private final String planName;

	public SubscribeEvent(IRCParser parser) {
		username = (parser.getUserName() != null) ? parser.getUserName() : parser.getTags().getTag("user").toString();
		channel = parser.getChannelName();
		plan = SubPlan.valueOf(parser.getTags().getTag("msg-param-sub-plan").toString());

		message = parser.getMessage();
		months = ((int) parser.getTags().getTag("msg-param-months") > 1) ? (int) parser.getTags().getTag("msg-param-months") : 1;
		tags = (message != null) ? parser.getTags() : null;
		planName = stringifyPlanName(parser.getTags().getTag("msg-param-sub-plan-name").toString());
	}

	private String stringifyPlanName(String planName) {
		return planName
				.replace("\\\\s", " ")
				.replace("\\\\:", ";")
				.replace("\\\\\\\\", "\\")
				.replace("\\r", "\r")
				.replace("\\n", "\n");
	}

	@Override
	public String toString() {

		return null;
	}
}
