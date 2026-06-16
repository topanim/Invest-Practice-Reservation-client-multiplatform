package app.what.reservation.core.foundation.data

sealed interface RemoteState {
    object Idle : RemoteState
    object Loading : RemoteState
    class Error(val e: Exception) : RemoteState
    object Success : RemoteState
    object Empty : RemoteState
}