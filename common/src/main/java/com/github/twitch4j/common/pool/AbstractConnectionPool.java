package com.github.twitch4j.common.pool;

import lombok.experimental.SuperBuilder;

/**
 * A pool for connections to be created and destroyed.
 *
 * @param <C> the connection type
 */
@SuperBuilder
public abstract class AbstractConnectionPool<C> implements AutoCloseable {

    protected abstract C createConnection();

    protected abstract void disposeConnection(C connection);

    protected abstract Iterable<C> getConnections();

    @Override
    public void close() {
        getConnections().forEach(this::disposeConnection);
    }

    /**
     * @return the number of open connections held by this pool.
     */
    public int numConnections() {
        int n = 0;
        for (C ignored : getConnections()) {
            n++;
        }
        return n;
    }

}
