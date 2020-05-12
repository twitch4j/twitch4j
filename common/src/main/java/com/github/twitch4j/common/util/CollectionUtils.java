package com.github.twitch4j.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CollectionUtils {

    public static <T> List<List<T>> chunked(Iterable<T> iterable, int size) {
        List<List<T>> chunks = new ArrayList<>();
        List<T> chunk = null;
        Iterator<T> it = iterable.iterator();
        for (int i = 0; it.hasNext(); i++) {
            if (i % size == 0) {
                chunk = new ArrayList<>();
                chunks.add(chunk);
            }

            chunk.add(it.next());
        }
        return chunks;
    }

}
