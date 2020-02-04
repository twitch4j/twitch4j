package com.github.twitch4j.modules.event;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.modules.IModule;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
abstract class ModuleEvent extends Event {
	private final IModule module;
}
