package com.github.twitch4j.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@UtilityClass
public class PaginationUtil {

    /**
     * Obtains a full (user-specified) collection of results from paginated calls with arbitrary cursors, up to a max number of pages and elements, while optionally avoiding duplicate cursors.
     * <p>
     * Implementation notes:
     * <ul>
     * <li>strict mode involves more memory usage but can avoid unnecessary loops when max pages and elements are high and the call can yield undesirable duplicate cursors</li>
     * <li>even without strict mode, this checks that the next cursor does not equal the previous cursor to avoid some loops while reducing memory footprint</li>
     * <li>the max elements constraint can be marginally violated when the final page of results had enough elements to go beyond this threshold</li>
     * <li>pagination stops if any threshold is met (it does not wait for both the max pages and max elements thresholds to be exceeded)</li>
     * <li>null cursors yielded from a call end further pagination</li>
     * </ul>
     *
     * @param callByCursor Performs a query based on the cursor
     * @param getResult    Yields the results contained in the executed call
     * @param getNext      Yields the next cursor to paginate on (or null to cease pagination)
     * @param maxPages     The maximum number of pages to paginate over
     * @param maxUnits     The (approximate) maximum number of elements that can be queried
     * @param collector    The supplier of the collection to store the paginated results
     * @param strict       Whether pagination should stop upon encountering an already-seen cursor
     * @param first        The initial cursor to use in the first call
     * @param valid        Whether a cursor is valid to be used in a subsequent call
     * @param <T>          Result unit class
     * @param <K>          Container class of results and pagination information
     * @param <C>          The type of collection that should be used to store the result units
     * @param <P>          The type of the cursor object; should implement equals and hashcode
     * @return the paginated results
     */
    public static <T, K, C extends Collection<T>, P> C getPaginated(Function<P, K> callByCursor, Function<K, Collection<T>> getResult, Function<K, P> getNext, int maxPages, int maxUnits, Supplier<C> collector, boolean strict, P first, Predicate<P> valid) {
        final C collection = collector.get();

        Set<P> cursors = strict ? new HashSet<>(Math.max(16, Math.min(maxPages, 1024))) : null;
        P cursor = first;
        int page = 0;
        do {
            // Perform the call
            final K call = callByCursor.apply(cursor);
            page++;
            if (call == null) break;

            // Save the results
            final Collection<T> results = getResult.apply(call);
            if (results == null || results.isEmpty()) break;
            collection.addAll(results);

            // Obtain the cursor for the next call
            final P next = getNext.apply(call);
            if (Objects.equals(next, cursor)) break; // we got the same cursor back; avoid infinite loop
            if (strict && next != null && !cursors.add(next)) break; // we've already seen this cursor in the past; avoid infinite loop
            cursor = next;
        } while (cursor != null && valid.test(cursor) && (maxPages < 0 || page < maxPages) && (maxUnits < 0 || collection.size() < maxUnits));

        return collection;
    }

    /**
     * Obtains a full (user-specified) collection of results from paginated calls, up to a max number of pages and elements, while optionally avoiding duplicate cursors.
     * <p>
     * Implementation notes:
     * <ul>
     * <li>null or empty cursors yielded from a call end further pagination</li>
     * <li>the first call executed is done with a null cursor</li>
     * </ul>
     *
     * @param callByCursor     Performs a query based on the string cursor (which is initially null)
     * @param extractResult    Yields the results contained in the executed call
     * @param extractCursor    Yields the next cursor to paginate on (or null to cease pagination)
     * @param maxPages         The maximum number of pages to paginate over
     * @param maxElements      The (approximate) maximum number of elements that can be queried
     * @param createCollection The supplier of the collection to store the paginated results
     * @param strict           Whether pagination should stop upon encountering an already-seen cursor
     * @param <T>              Result unit class
     * @param <K>              Container class of results and pagination information
     * @param <C>              The type of collection that should be used to store the result units
     * @return the paginated results
     * @see #getPaginated(Function, Function, Function, int, int, Supplier, boolean, Object, Predicate)
     */
    public static <T, K, C extends Collection<T>> C getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> extractResult, Function<K, String> extractCursor, int maxPages, int maxElements, Supplier<C> createCollection, boolean strict) {
        return getPaginated(callByCursor, extractResult, extractCursor, maxPages, maxElements, createCollection, strict, null, s -> !s.isEmpty());
    }

    /**
     * Obtains a full (user-specified) collection of results from paginated calls, up to a max number of pages and elements.
     *
     * @param callByCursor       Performs a query based on the string cursor (which is initially null)
     * @param resultsFromCall    Yields the results contained in the executed call
     * @param nextCursorFromCall Yields the next cursor to paginate on (or null to cease pagination)
     * @param maxPages           The maximum number of pages to paginate over
     * @param maxElements        The (approximate) maximum number of elements that can be queried
     * @param createCollection   The supplier of the collection to store the paginated results
     * @param <T>                Result unit class
     * @param <K>                Container class of results and pagination information
     * @param <C>                The type of collection that should be used to store the result units
     * @return the paginated results
     * @see #getPaginated(Function, Function, Function, int, int, Supplier, boolean)
     */
    public static <T, K, C extends Collection<T>> C getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> resultsFromCall, Function<K, String> nextCursorFromCall, int maxPages, int maxElements, Supplier<C> createCollection) {
        return getPaginated(callByCursor, resultsFromCall, nextCursorFromCall, maxPages, maxElements, createCollection, false);
    }

    /**
     * Obtains a full list of results from paginated calls, up to a max number of pages and elements.
     *
     * @param callByCursor       Performs a query based on the string cursor (which is initially null)
     * @param resultsFromCall    Yields the results contained in the executed call
     * @param nextCursorFromCall Yields the next cursor to paginate on (or null to cease pagination)
     * @param maxPages           The maximum number of pages to paginate over
     * @param maxElements        The (approximate) maximum number of elements that can be queried
     * @param <T>                Result unit class
     * @param <K>                Container class of results and pagination information
     * @return the paginated results
     * @see #getPaginated(Function, Function, Function, int, int, Supplier, boolean)
     */
    public static <T, K> List<T> getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> resultsFromCall, Function<K, String> nextCursorFromCall, int maxPages, int maxElements) {
        return getPaginated(callByCursor, resultsFromCall, nextCursorFromCall, maxPages, maxElements, ArrayList::new);
    }

    /**
     * Obtains a full list of results from paginated calls, up to a max number of pages.
     *
     * @param callByCursor       Performs a query based on the string cursor (which is initially null)
     * @param resultsFromCall    Yields the results contained in the executed call
     * @param nextCursorFromCall Yields the next cursor to paginate on (or null to cease pagination)
     * @param maxPages           The maximum number of pages to paginate over
     * @param <T>                Result unit class
     * @param <K>                Container class of results and pagination information
     * @return the paginated results
     * @see #getPaginated(Function, Function, Function, int, int, Supplier, boolean)
     */
    public static <T, K> List<T> getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> resultsFromCall, Function<K, String> nextCursorFromCall, int maxPages) {
        return getPaginated(callByCursor, resultsFromCall, nextCursorFromCall, maxPages, Integer.MAX_VALUE);
    }

    /**
     * Obtains a full list of results from paginated calls.
     *
     * @param callByCursor       Performs a query based on the string cursor (which is initially null)
     * @param resultsFromCall    Yields the results contained in the executed call
     * @param nextCursorFromCall Yields the next cursor to paginate on (or null to cease pagination)
     * @param <T>                Result unit class
     * @param <K>                Container class of results and pagination information
     * @return the paginated results
     * @see #getPaginated(Function, Function, Function, int, int, Supplier, boolean)
     */
    public static <T, K> List<T> getPaginated(Function<String, K> callByCursor, Function<K, Collection<T>> resultsFromCall, Function<K, String> nextCursorFromCall) {
        return getPaginated(callByCursor, resultsFromCall, nextCursorFromCall, Integer.MAX_VALUE);
    }

}
