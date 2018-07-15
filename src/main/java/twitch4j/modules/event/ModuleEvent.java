package twitch4j.modules.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import twitch4j.events.Event;
import twitch4j.modules.IModule;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ModuleEvent extends Event{
	private final IModule module;
}
