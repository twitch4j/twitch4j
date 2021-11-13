package com.github.twitch4j.eventsub.condition;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * @see <a href="https://dev.twitch.tv/docs/eventsub/eventsub-reference#goals-condition">Official docs</a>
 */
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Jacksonized
public class CreatorGoalsCondition extends ChannelEventSubCondition {}
