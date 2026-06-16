package app.what.reservation.features.serviceDetail.domain.models

import app.what.reservation.core.foundation.data.RemoteState
import app.what.reservation.domain.models.AvailabilityBlock
import app.what.reservation.domain.models.AvailabilityRule
import app.what.reservation.domain.models.BookingDomain
import app.what.reservation.domain.models.OneTimeAvailabilityRule
import app.what.reservation.domain.models.ServiceDomain
import app.what.reservation.domain.models.UserDomain
import kotlinx.datetime.LocalDateTime

data class ServiceDetailState(
    val service: ServiceDomain? = null,
    val serviceState: RemoteState = RemoteState.Idle,
    val isAdmin: Boolean = false,
    val admins: List<UserDomain> = emptyList(),
    val adminsState: RemoteState = RemoteState.Idle,
    val availabilityRules: List<AvailabilityRule> = emptyList(),
    val availabilityOneTimeRules: List<OneTimeAvailabilityRule> = emptyList(),
    val availabilityBlockRules: List<AvailabilityBlock> = emptyList(),
    val availabilityRulesState: RemoteState = RemoteState.Idle,
    val userSearchResult: List<UserDomain> = emptyList(),
    val slotsState: RemoteState = RemoteState.Idle,
    val slots: List<Slot> = emptyList(),
    val myReservations: List<BookingDomain> = emptyList(),
    val allReservations: List<BookingDomain> = emptyList(),
    val userCacheInfo: Set<UserDomain> = emptySet()
)

data class Slot(
    val from: LocalDateTime,
    val to: LocalDateTime
)
