package app.what.reservation.features.profile.domain.models

import app.what.reservation.core.foundation.data.RemoteState
import app.what.reservation.domain.models.BookingDomain
import app.what.reservation.domain.models.UserDomain
import kotlin.uuid.Uuid

data class ProfileState(
    val infoState: RemoteState = RemoteState.Idle,
    val info: ProfileInfo? = null,
    val myReservations: List<BookingDomain> = emptyList(),
    val myReservationsState: RemoteState = RemoteState.Idle,
    val userCacheInfo: Set<UserDomain> = emptySet()
)

data class ProfileInfo(
    val id: Uuid,
    val name: String,
    val email: String,
    val picture: String? = null
)