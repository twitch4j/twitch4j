package com.github.twitch4j.modules.event;

import com.github.philippheuer.events4j.core.domain.Event;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import com.github.twitch4j.modules.IModule;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
abstract class ModuleEvent extends Event {
	private final IModule module;
}
