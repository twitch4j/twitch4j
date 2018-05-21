package twitch4j.common.events;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@SuppressWarnings({"unchecked"})
public class Manager {

	private final Scheduler scheduler;
	private final PublishProcessor<Event<?>> processor;

	private final AnnotationEvents annotationEvents = new AnnotationEvents(this);
	private final ListenerEvents listenerEvents = new ListenerEvents(this);

	public Manager(Scheduler scheduler, PublishProcessor<Event<?>> processor) {
		this.scheduler = scheduler;
		this.processor = processor;

		registerInternalListener();
	}

	public Manager() {
		this(Schedulers.io(), PublishProcessor.create());
	}

	public <E extends Event<E>> void dispatch(E event) {
		// - created At
		event.setCreatedAt(Instant.now());
		// - unique event id
		event.setEventId(UUID.randomUUID().toString());

		Flowable<Event<E>> eventFlow = Flowable.just(event);
		eventFlow.subscribeWith(processor).subscribe();
	}

	public <E extends Event<E>> Flowable<E> onEvent(Class<E> eventClass) {
		return processor.observeOn(scheduler).ofType(eventClass);
	}

	private void registerInternalListener() {
		onEvent(Event.class).subscribe(event -> {
			log.debug("Passed event [{}] to the method based annotation manager at {}.", event.getEventId(), event.getCreatedAt().toString());
			annotationEvents.dispatch(event);
			listenerEvents.dispatch(event);
		});
	}
}
