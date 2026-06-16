package app.what.reservation.core.foundation.utils

import kotlin.time.Clock

fun measureTime(block: () -> Unit): Long {
    val startTime = Clock.System.now().toEpochMilliseconds()
    block()
    val endTime = Clock.System.now().toEpochMilliseconds()
    return endTime - startTime
}

suspend fun suspendMeasureTime(block: suspend () -> Unit): Long {
    val startTime = Clock.System.now().toEpochMilliseconds()
    block()
    val endTime = Clock.System.now().toEpochMilliseconds()
    return endTime - startTime
}