package me.philippheuer.twitch4j.events;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import net.jodah.typetools.TypeResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages event listeners and event logic.
 *
 * @author Austin [https://github.com/austinv11]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Setter
public class EventDispatcher {

	private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<Method, CopyOnWriteArrayList<ListenerPair<Object>>>> methodListeners = new ConcurrentHashMap<>();
	@SuppressWarnings("rawtypes")
	private final ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<ListenerPair<IListener>>> classListeners = new ConcurrentHashMap<>();
	private final ExecutorService eventExecutor = Executors.newCachedThreadPool(runnable -> { //Ensures all threads are daemons
		Thread thread = Executors.defaultThreadFactory().newThread(runnable);
		thread.setName("Event Dispatch Thread");
		thread.setDaemon(true);
		return thread;
	});

	/**
	 * Holds the TwitchClient Instance
	 */
	private TwitchClient client;

	/**
	 * Class Constructor
	 *
	 * @param client The Twitch4J Client.
	 */
	public EventDispatcher(TwitchClient client) {
		setClient(client);
	}

	/**
	 * Registers a listener using {@link EventSubscriber} method annotations.
	 *
	 * @param listener The listener.
	 */
	public void registerListener(Object listener) {
		registerListener(listener.getClass(), listener, false);
	}

	/**
	 * Registers a listener using {@link EventSubscriber} method annotations.
	 *
	 * @param listener The listener.
	 */
	public void registerListener(Class<?> listener) {
		registerListener(listener, null, false);
	}

	/**
	 * Registers a single event listener.
	 *
	 * @param listener The listener.
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void registerListener(IListener listener) {
		registerListener(listener, false);
	}

	/**
	 * Registers a single event listener.
	 *
	 * @param listenerClass The class of the listener.
	 * @param listener The listener.
	 * @param isTemporary Whether the listener is temporary or not.
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void registerListener(Class<?> listenerClass, Object listener, boolean isTemporary) {
		if (IListener.class.isAssignableFrom(listenerClass)) {
			Logger.warn(this, "IListener was attempted to be registered as an annotation listener. The listener in question will now be registered as an IListener.");
			registerListener((IListener) listener, isTemporary);
			return;
		}

		for (Method method : listenerClass.getMethods()) {
			if (method.getParameterCount() == 1
					&& method.isAnnotationPresent(EventSubscriber.class)) {
				if ((Modifier.isStatic(method.getModifiers()) && listener == null) || listener != null) {
					method.setAccessible(true);
					Class<?> eventClass = method.getParameterTypes()[0];
					if (Event.class.isAssignableFrom(eventClass)) {
						if (!methodListeners.containsKey(eventClass))
							methodListeners.put(eventClass, new ConcurrentHashMap<>());

						if (!methodListeners.get(eventClass).containsKey(method))
							methodListeners.get(eventClass).put(method, new CopyOnWriteArrayList<>());

						methodListeners.get(eventClass).get(method).add(new ListenerPair<>(isTemporary, listener));
						Logger.info(this, "Registered method listener %s#%s", listenerClass.getSimpleName(), method.getName());
					}
				}
			}
		}
	}

	/**
	 * Registers a single event listener.
	 *
	 * @param listener The listener.
	 * @param isTemporary Whether the listener is temporary or not.
	 */
	private <T extends Event> void registerListener(IListener<T> listener, boolean isTemporary) {
		Class<?> rawType = TypeResolver.resolveRawArgument(IListener.class, listener.getClass());
		if (Event.class.isAssignableFrom(rawType)) {
			if (!classListeners.containsKey(rawType))
				classListeners.put(rawType, new CopyOnWriteArrayList<>());

			Logger.info(this, "Registered IListener %s", listener.getClass().getSimpleName());
			classListeners.get(rawType).add(new ListenerPair<>(isTemporary, listener));
		}
	}

	/**
	 * This registers a temporary event listener using {@link EventSubscriber} method annotations.
	 * Meaning that when it listens to an event, it immediately unregisters itself.
	 *
	 * @param listener The listener.
	 */
	public void registerTemporaryListener(Object listener) {
		registerListener(listener.getClass(), listener, true);
	}

	/**
	 * This registers a temporary event listener using {@link EventSubscriber} method annotations.
	 * Meaning that when it listens to an event, it immediately unregisters itself.
	 *
	 * @param listener The listener.
	 */
	public void registerTemporaryListener(Class<?> listener) {
		registerListener(listener, null, true);
	}

	/**
	 * This registers a temporary single event listener.
	 * Meaning that when it listens to an event, it immediately unregisters itself.
	 *
	 * @param <T> Type of the temporary event.
	 * @param listener The listener.
	 */
	public <T extends Event> void registerTemporaryListener(IListener<T> listener) {
		registerListener(listener, true);
	}

	/**
	 * Unregisters a listener using {@link EventSubscriber} method annotations.
	 *
	 * @param listener The listener.
	 */
	@SuppressWarnings("rawtypes")
	public void unregisterListener(Object listener) {
		for (Method method : listener.getClass().getMethods()) {
			if (method.getParameterCount() == 1) {
				Class<?> eventClass = method.getParameterTypes()[0];
				if (Event.class.isAssignableFrom(eventClass)) {
					if (methodListeners.containsKey(eventClass)) {
						if (methodListeners.get(eventClass).containsKey(method)) {
							methodListeners.get(eventClass).get(method).removeIf((ListenerPair pair) -> pair.listener == listener); //Yes, the == is intentional. We want the exact same instance.

							Logger.debug(this, "Unregistered method listener %s#%s", listener.getClass().getSimpleName(), method.toString());
						}
					}
				}
			}
		}
	}

	/**
	 * Unregisters a listener using {@link EventSubscriber} method annotations.
	 *
	 * @param clazz The listener class with static methods.
	 */
	@SuppressWarnings("rawtypes")
	public void unregisterListener(Class<?> clazz) {
		for (Method method : clazz.getMethods()) {
			if (method.getParameterCount() == 1) {
				Class<?> eventClass = method.getParameterTypes()[0];
				if (Event.class.isAssignableFrom(eventClass)) {
					if (methodListeners.containsKey(eventClass))
						if (methodListeners.get(eventClass).containsKey(method)) {
							methodListeners.get(eventClass).get(method).removeIf((ListenerPair pair) -> pair.listener == null); // null for static listener

							Logger.debug(this, "Unregistered class method listener %s#%s", clazz.getSimpleName(), method.toString());
						}
				}
			}
		}
	}

	/**
	 * Unregisters a single event listener.
	 *
	 * @param listener The listener.
	 */
	@SuppressWarnings("rawtypes")
	public void unregisterListener(IListener listener) {
		Class<?> rawType = TypeResolver.resolveRawArgument(IListener.class, listener.getClass());
		if (Event.class.isAssignableFrom(rawType)) {
			if (classListeners.containsKey(rawType)) {
				classListeners.get(rawType).removeIf((ListenerPair pair) -> pair.listener == listener); //Yes, the == is intentional. We want the exact same instance.

				Logger.debug(this, "Unregistered IListener %s", listener.getClass().getSimpleName());
			}
		}
	}

	/**
	 * Dispatches an event.
	 *
	 * @param event The event.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void dispatch(Event event) {
		eventExecutor.submit(() -> {
			Logger.trace(this, "Dispatching Event of Type [%s]", event.getClass().getSimpleName());
			event.setClient(client);

			// Method Listener
			methodListeners.entrySet().stream()
					.filter(e -> e.getKey().isAssignableFrom(event.getClass()))
					.map(Map.Entry::getValue)
					.forEach(m ->
							m.forEach((k, v) ->
									v.forEach(o -> {
										try {
											// Invoke Event
											k.invoke(o.listener, event);

											// Remove Temporary Listener
											if (o.isTemporary) {
												unregisterListener(o.listener);
											}
										} catch (IllegalAccessException ex) {
											Logger.error(this, "Error dispatching event %s", event.getClass().getSimpleName());
										} catch (Exception ex) {
											ex.printStackTrace();
											Logger.error(this, "Unhandled exception caught dispatching event %s", event.getClass().getSimpleName()); // e.getCause()
										}
									})));

			// Class Listener
			classListeners.entrySet().stream()
					.filter(e -> e.getKey().isAssignableFrom(event.getClass()))
					.map(Map.Entry::getValue)
					.forEach(s -> s.forEach(l -> {
						try {
							// Invoke Event
							l.listener.handle(event);

							// Remove Temporary Listener
							if (l.isTemporary)
								unregisterListener(l.listener);
						} catch (ClassCastException e) {
							// FIXME: This occurs when a lambda expression is used to create an IListener leading it to be registered under the type 'Event'.
							// FIXME: This is due to a bug in TypeTools: https://github.com/jhalterman/typetools/issues/14
						} catch (Exception ex) {
							Logger.error(this, "Unhandled exception caught dispatching event %s [%s]", event.getClass().getSimpleName(), ex.getMessage());
						}
					}));
		});
	}

	/**
	 * This is used to differentiate temporary event listeners from permanent ones.
	 *
	 * @param <V> The type of listener, either {@link Object} or {@link IListener}
	 */
	private static class ListenerPair<V> {

		/**
		 * Whether the listener is temporary.
		 * True if a temporary listener, false if otherwise.
		 */
		public final boolean isTemporary;

		/**
		 * The actual listener object instance.
		 */
		public final V listener;

		/**
		 * Class Constructor
		 *
		 * @param isTemporary Whether the listener is temporary or not.
		 * @param listener The listener.
		 */
		private ListenerPair(boolean isTemporary, V listener) {
			this.isTemporary = isTemporary;
			this.listener = listener;
		}
	}
}
