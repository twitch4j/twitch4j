package com.github.twitch4j.helix.eventsub.condition;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class StreamOnlineCondition extends ChannelEventSubCondition {}
