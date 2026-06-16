package app.what.reservation.features.serviceDetail.domain.models

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.uuid.Uuid

sealed interface ServiceDetailEvent {
    object OnRefresh : ServiceDetailEvent
    class OnCancelBookingAsUser(val id: Uuid) : ServiceDetailEvent
    class OnCancelBookingAsAdmin(val id: Uuid, val reason: String) : ServiceDetailEvent
    object OnAddAdminFormOpen : ServiceDetailEvent
    class OnUserSearchConfirm(val query: String) : ServiceDetailEvent
    class OnBookingDateChoiceComplete(val date: LocalDate) : ServiceDetailEvent
    class OnBookingFormConfirmed(val slot: Slot) : ServiceDetailEvent
    class OnAddAdminFormConfirmed(val adminIds: List<Uuid>) : ServiceDetailEvent
    class OnRemoveAdminClicked(val id: Uuid) : ServiceDetailEvent
    class OnRemoveAvailabilityRuleClicked(val id: Uuid) : ServiceDetailEvent
    class OnRemoveAvailabilityOneTimeRuleClicked(val id: Uuid) : ServiceDetailEvent
    class OnRemoveAvailabilityBlockClicked(val id: Uuid) : ServiceDetailEvent
    class OnAddAvailabilityRuleFormConfirmed(
        val daysOfWeek: List<DayOfWeek>,
        val startTime: LocalTime,
        val endTime: LocalTime
    ) : ServiceDetailEvent

    class OnAddOneTimeAvailabilityRuleFormConfirmed(
        val dateFrom: LocalDate,
        val dateTo: LocalDate,
        val startTime: LocalTime,
        val endTime: LocalTime
    ) : ServiceDetailEvent

    class OnAddAvailabilityBlockFormConfirmed(
        val from: LocalDateTime,
        val to: LocalDateTime,
        val reason: String?
    ) : ServiceDetailEvent
}