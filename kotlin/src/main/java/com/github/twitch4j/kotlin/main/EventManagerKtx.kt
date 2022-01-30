package com.github.twitch4j.kotlin.main

import com.github.philippheuer.events4j.core.EventManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Creates a flow for a specific event.
 *
 * Automatically disposes of the subscription when there are no more collectors.
 *
 * @return  A Flow object that encapsulates the eventListener and handles the subscription
 */
@Deprecated("Remove when kotlin module was merged in events4j")
inline fun <reified T> EventManager.flowOn(): Flow<T> = flowOn(T::class.java)

/**
 * Creates a flow for a specific event.
 *
 * Automatically disposes of the subscription when there are no more collectors.
 *
 * @param klass The class to receive events for
 * @return      A Flow object that encapsulates the eventListener and handles the subscription
 */
@Deprecated("Remove when kotlin module was merged in events4j")
fun <T> EventManager.flowOn(
	klass: Class<T>
): Flow<T> = callbackFlow {
	val subscription = onEvent(klass, ::trySend)

	awaitClose {
		subscription.dispose()
	}
}
