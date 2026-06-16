package app.what.reservation.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object AppDispatchers {
    actual val IO = Dispatchers.IO
}