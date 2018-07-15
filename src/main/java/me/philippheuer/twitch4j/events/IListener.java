package me.philippheuer.twitch4j.events;

/**
 * Used to represent a class that handles only one event.
 *
 * @param <T> The event type to handle.
 *
 * @author Austin [https://github.com/austinv11]
 * @version %I%, %G%
 * @since 1.0
 */
@FunctionalInterface
public interface IListener<T extends Event> {

	/**
	 * Called when the event is sent.
	 *
	 * @param event The event object.
	 */
	void handle(T event);

}
