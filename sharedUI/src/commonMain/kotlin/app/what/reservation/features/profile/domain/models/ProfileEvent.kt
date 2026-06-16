package app.what.reservation.features.profile.domain.models

import kotlin.uuid.Uuid

sealed interface ProfileEvent {
    object OnRefresh : ProfileEvent
    object OnLogout : ProfileEvent
    object OnOpen : ProfileEvent
    class OnCancelBookingAsUser(val id: Uuid) : ProfileEvent
}