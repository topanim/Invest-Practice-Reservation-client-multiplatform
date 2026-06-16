package app.what.reservation.features.profile.domain.models

sealed interface ProfileAction {
    object NavigateToAuth : ProfileAction
}