package twitch4j.common.events;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Getter
public class EventManager<E extends Event> {

	private final Scheduler scheduler;
	private final FluxProcessor<E, E> processor;

	private final AnnotationEvents annotationEvents = new AnnotationEvents(this);
	private final ListenerEvents listenerEvents = new ListenerEvents(this);

	public EventManager(Scheduler scheduler, FluxProcessor<E, E> processor) {
		this.scheduler = scheduler;
		this.processor = processor;

		registerInternalListener();
	}

	public EventManager() {
		this(Schedulers.parallel(), EmitterProcessor.create(256, false));
	}

	public void dispatch(E event) {
		// - created At
		event.setCreatedAt(Instant.now());
		// - unique event id
		event.setEventId(UUID.randomUUID().toString());

		Mono<E> eventMono = Mono.just(event);
		eventMono.subscribeWith(processor).subscribe();
	}

	public Flux<E> onEvent(Class<E> eventClass) {
		return processor.publishOn(scheduler).ofType(eventClass);
	}

	@SuppressWarnings("unchecked")
	private void registerInternalListener() {
		onEvent((Class<E>) Event.class).subscribe(event -> {
			log.debug("Passed event [{}] to the method based annotation manager at {}.", event.getEventId(), event.getCreatedAt().toString());
			annotationEvents.dispatch(event);
			listenerEvents.dispatch(event);
		});
	}

	public void registerListeners(Collection<Object> listeners) {
		listeners.forEach(this::registerListener);
	}

	public void registerListeners(Object... listeners) {
		registerListeners(Arrays.asList(listeners));
	}

	@SuppressWarnings("unchecked")
	public void registerListener(Object listener) {
		if (IListener.class.isAssignableFrom(listener.getClass())) {
			listenerEvents.registerListener((IListener<E>) listener);
		} else {
			annotationEvents.registerListener(listener);
		}
	}


}
