package twitch4j.api.helix.service;

import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.route.Route;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractSizedService<T> extends AbstractService<T> {
	protected final AtomicInteger size = new AtomicInteger(20);

	AbstractSizedService(Route<T> route, Router router) {
		super(route, router);
	}

	/**
	 * Limit data for pagination.
	 * @param size <b>Max:</b> 100,<br> <b>Default:</b> 20
	 * @return
	 */
	public <S extends AbstractSizedService<T>> S size(int size) {
		if (size > 100) {
			this.size.set(100);
		} else if (size < 1) {
			this.size.set(20);
		} else {
			this.size.set(size);
		}
		return (S) this;
	}
}
