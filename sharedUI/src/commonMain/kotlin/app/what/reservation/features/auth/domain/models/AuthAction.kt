package app.what.reservation.features.auth.domain.models

sealed interface AuthAction {
    object NavigateToMain : AuthAction
}