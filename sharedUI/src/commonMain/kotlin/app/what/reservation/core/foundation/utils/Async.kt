package app.what.reservation.core.foundation.utils

import app.what.reservation.utils.AppDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit) =
    launch(AppDispatchers.IO, block = block)

fun CoroutineScope.delayLaunch(timeMillis: Long, block: suspend () -> Unit) = launch {
    delay(timeMillis)
    block()
}

fun <T> CoroutineScope.asyncLazy(
    context: CoroutineContext = AppDispatchers.IO,
    block: suspend () -> T
): Lazy<Deferred<T>> = lazy {
    async(context, start = CoroutineStart.LAZY) { block() }
}

fun CoroutineScope.launchSafe(
    context: CoroutineContext = AppDispatchers.IO,
    retryCount: Int = 5,
    debug: Boolean = false,
    onFailure: suspend CoroutineScope.(Exception) -> Unit = {},
    onFinally: suspend CoroutineScope.() -> Unit = {},
    block: suspend CoroutineScope.(attempt: Int) -> Unit
): Job = launch(context) {
    try {
        repeat(retryCount + 1) { attempt ->
            try {
                block(attempt)
                return@launch
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (debug) throw e
                else if (attempt != retryCount) return@repeat
                else onFailure(e)
            }
        }
    } finally {
        onFinally()
    }
}