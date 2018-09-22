package me.philippheuer.twitch4j.modules.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.philippheuer.twitch4j.events.event.TwitchEvent;
import me.philippheuer.twitch4j.modules.IModule;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ModuleEvent extends TwitchEvent {
	private final IModule module;
}
