package app.what.reservation.utils

import kotlinx.coroutines.CoroutineDispatcher

expect object AppDispatchers {
    val IO: CoroutineDispatcher
}