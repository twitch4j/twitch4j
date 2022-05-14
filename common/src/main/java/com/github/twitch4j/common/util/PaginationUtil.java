package com.github.twitch4j.common.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class PaginationUtil {

    public static <T, K, C extends Collection<T>> C getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> extractResult, Function<K, String> extractCursor, int maxPages, int maxElements, Supplier<C> collectionFactory, boolean strict) {
        final C collection = collectionFactory.get();

        Set<String> cursors = strict ? new HashSet<>(maxPages) : null;
        String cursor = null;
        int page = 0;
        do {
            final K call = callByCursor.apply(cursor);
            page++;
            if (call == null) break;

            final Collection<T> results = extractResult.apply(call);
            if (results == null || results.isEmpty()) break;
            collection.addAll(results);

            final String next = extractCursor.apply(call);
            if (Objects.equals(next, cursor)) break; // we got the same cursor back; avoid infinite loop
            if (strict && cursor != null && !cursors.add(cursor)) break; // we've already seen this cursor in the past; avoid infinite loop
            cursor = next;
        } while (cursor != null && !cursor.isEmpty() && page < maxPages && collection.size() < maxElements);

        return collection;
    }

    public static <T, K, C extends Collection<T>> C getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> resultsFromCall, Function<K, String> nextCursorFromCall, int maxPages, int maxElements, Supplier<C> createCollection) {
        return getPaginated(callByCursor, resultsFromCall, nextCursorFromCall, maxPages, maxElements, createCollection, false);
    }

    public static <T, K> List<T> getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> resultsFromCall, Function<K, String> nextCursorFromCall, int maxPages, int maxElements) {
        return getPaginated(callByCursor, resultsFromCall, nextCursorFromCall, maxPages, maxElements, ArrayList::new);
    }

    public static <T, K> List<T> getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> resultsFromCall, Function<K, String> nextCursorFromCall, int maxPages) {
        return getPaginated(callByCursor, resultsFromCall, nextCursorFromCall, maxPages, Integer.MAX_VALUE);
    }

    public static <T, K> List<T> getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> resultsFromCall, Function<K, String> nextCursorFromCall) {
        return getPaginated(callByCursor, resultsFromCall, nextCursorFromCall, Integer.MAX_VALUE);
    }

}
