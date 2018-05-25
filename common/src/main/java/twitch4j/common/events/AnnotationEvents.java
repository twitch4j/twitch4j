package twitch4j.common.events;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;

@Slf4j
@RequiredArgsConstructor
public class AnnotationEvents<E extends Event> {
	private final EventManager manager;

	/**
	 * Annotation based method listeners
	 */
	private final ConcurrentHashMap<Class<E>,
			ConcurrentHashMap<Method, CopyOnWriteArrayList<Object>>> methodListeners = new ConcurrentHashMap<>();

	/**
	 * Registers a listener using {@link EventSubscriber} method annotations.
	 *
	 * @param eventListener The class instance annotated with {@link EventSubscriber} annotations.
	 */
	public void registerListener(Object eventListener) {
		registerListener(eventListener.getClass(), eventListener);
	}

	/**
	 * Registers a single event listener.
	 *
	 * @param eventListenerClass The class of the listener.
	 * @param eventListener      The class instance of the listener.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void registerListener(Class<?> eventListenerClass, Object eventListener) {
		// for each method on the event listener class
		for (Method method : eventListenerClass.getMethods()) {
			// event subscribers need exactly one parameter
			if (method.getParameterCount() == 1) {
				// event subscriber methods need to be annotated with @EventSubscriber
				if (method.isAnnotationPresent(EventSubscriber.class)) {
					// ignore access checks so that we can call private methods
					method.setAccessible(true);

					// get event class the listener expects
					Class<E> eventClass = (Class<E>) method.getParameterTypes()[0];

					// check if the event class extends the base event class
					if (Event.class.isAssignableFrom(eventClass)) {

						// add class to methodListeners
						if (!methodListeners.containsKey(eventClass))
							methodListeners.put(eventClass, new ConcurrentHashMap<>());

						// add method to methodListeners
						if (!methodListeners.get(eventClass).containsKey(method))
							methodListeners.get(eventClass).put(method, new CopyOnWriteArrayList<>());

						// add event listener to method
						methodListeners.get(eventClass).get(method).add(eventListener);

						// log
						log.info("Registered method listener {}#{}", eventListenerClass.getSimpleName(), method.getName());
					}
				}
			}
		}
	}

	/**
	 * Dispatched a event to the annotation based method listeners.
	 *
	 * @param event The event that will be dispatched to the annotation based method listeners.
	 */
	public synchronized void dispatch(E event) {
		// Call Method Listeners
		methodListeners.entrySet().stream()
				.filter(e -> e.getKey().isAssignableFrom(event.getClass()))
				.map(Map.Entry::getValue)
				.forEach(eventClass ->
						eventClass.forEach((k, v) ->
								v.forEach(object -> {
									try {
										// Invoke Event
										k.invoke(object, event);
									} catch (IllegalAccessException ex) {
										log.error("Error dispatching event {}.", event.getClass().getSimpleName());
										throw Exceptions.propagate(ex);
									} catch (Exception ex) {
										log.error("Unhandled exception caught dispatching event {}.", event.getClass().getSimpleName());
										throw Exceptions.propagate(ex);
									}
								})
						)
				);
	}

}
