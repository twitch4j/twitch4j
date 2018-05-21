package twitch4j.common.events;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class ListenerEvents {
	private final Manager manager;

	private final ConcurrentHashMap<Class<? extends Event>, Set<Consumer<Event>>> listeners = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public void registerListener(Consumer<Event> listener) {
		Class<? extends Event> eventClass = getSubtype(listener.getClass());
		if (listeners.containsKey(eventClass) && listeners.get(eventClass) != null)
			listeners.get(eventClass).add(listener);
		else
			listeners.put(eventClass, new LinkedHashSet<>(Collections.singletonList(listener)));
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Event> getSubtype(Class<? extends Consumer> consumerClass) {
		return (Class<Event>) ((ParameterizedType) consumerClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public synchronized <E extends Event<E>> void dispatch(E event) {
		listeners.entrySet().stream()
				.filter(e -> e.getKey().isAssignableFrom(event.getClass()))
				.map(Map.Entry::getValue)
				.forEach(listeners -> {
					listeners.forEach(listener -> {
						try {
							listener.accept(event);
						} catch (Exception e) {
							Exceptions.propagate(e);
						}
					});
				});
	}
}
