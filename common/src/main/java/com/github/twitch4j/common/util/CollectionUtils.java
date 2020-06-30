package com.github.twitch4j.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectionUtils {

    /**
     * Assigns elements of the given iterable to chunks not exceeding the desired size
     *
     * @param <T> type of the iterable
     * @param iterable the source of elements to be assigned to a chunk
     * @param size the maximum size of each chunk
     * @return a list of the chunks, or an empty list if the iterable yielded no elements
     * @throws NullPointerException if the passed iterable is null
     */
    public static <T> List<List<T>> chunked(Iterable<T> iterable, int size) {
        List<List<T>> chunks = new ArrayList<>();
        List<T> chunk = null;
        Iterator<T> it = iterable.iterator();
        for (int i = 0; it.hasNext(); i++) {
            if (i % size == 0) {
                chunk = new ArrayList<>(size);
                chunks.add(chunk);
            }

            chunk.add(it.next());
        }
        return chunks;
    }

}
