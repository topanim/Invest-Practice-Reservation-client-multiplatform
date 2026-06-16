package app.what.reservation.data.remote.dto

import app.what.reservation.domain.models.BookingStatus
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class BookingDto(
    val id: Uuid,
    val userId: Uuid,
    val serviceId: Uuid,
    val startsAt: LocalDateTime,
    val endsAt: LocalDateTime,
    val status: BookingStatus,
    val cancelReason: String? = null,
)

@Serializable
data class CreateBookingRequest(
    val serviceId: Uuid,
    val startsAt: LocalDateTime
)

@Serializable
data class BookingSlot(
    val from: LocalDateTime,
    val to: LocalDateTime
)
