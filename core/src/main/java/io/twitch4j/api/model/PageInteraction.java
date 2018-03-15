package io.twitch4j.api.model;

public interface PageInteraction<E, I> {
    PaginatedList<E, I> invoke(I cursor, boolean previous);

    default PaginatedList<E, I> invoke(I cursor) {
        return invoke(cursor, false);
    }

    interface CursorPage<E> extends PageInteraction<E, String> {
    }

    interface OffsetPage<E> extends PageInteraction<E, Integer> {
    }
}
