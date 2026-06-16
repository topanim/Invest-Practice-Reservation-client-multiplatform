package app.what.reservation.features.auth.domain.models

sealed interface AuthEvent {
    object OnSignWithGoogleClicked : AuthEvent
}