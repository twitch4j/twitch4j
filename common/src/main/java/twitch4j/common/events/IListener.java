package twitch4j.common.events;

/**
 * Used to represent a class that handles only one event.
 *
 * @param <E> The event type to handle.
 *
 * @author Austin [https://github.com/austinv11]
 * @version %I%, %G%
 * @since 1.0
 */
@FunctionalInterface
public interface IListener<E extends Event<E>> {

	/**
	 * Called when the event is sent.
	 *
	 * @param event The event object.
	 */
	void handle(E event);

}
