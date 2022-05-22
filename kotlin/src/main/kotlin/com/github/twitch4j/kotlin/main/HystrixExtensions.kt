package com.github.twitch4j.kotlin.main

import com.netflix.hystrix.HystrixCommand
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A simple wrapper function that will default to dispatch the execute job in the IO dispatcher.
 * Also see [HystrixCommand#execute]
 *
 * @param dispatcher The dispatcher to run in
 * @return           The result of execute
 */
suspend fun <T> HystrixCommand<T>.get(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): T = withContext(dispatcher) {
    execute()
}
