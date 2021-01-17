package com.github.twitch4j.common.pool;

/**
 * Generic interface for objects that can respond to a subscription request, S,
 * with a response, T, which can later be used for unsubscription to yield U.
 *
 * @param <S> subscription request
 * @param <T> transactional response to the subscription request that can be used as an unsubscription request
 * @param <U> unsubscription response
 */
public interface TransactionalSubscriber<S, T, U> {

    /**
     * Submits a subscription request.
     *
     * @param s the subscription request
     * @return a response to the request
     */
    T subscribe(S s);

    /**
     * Submits an unsubscription request.
     *
     * @param t the unsubscription request
     * @return a response to the request
     */
    U unsubscribe(T t);

}
