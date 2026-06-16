package app.what.reservation.domain.models

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class ServiceDomain(
    val id: Uuid,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val price: Float = 0f,
    val bufferMinutes: Int = 0,
    val isActive: Boolean = true,
)

@Serializable
data class UserDomain(
    val id: Uuid,
    val name: String,
    val email: String,
    val avatar: String?
)

@Serializable
data class AvailabilityRule(
    val id: Uuid,
    val serviceId: Uuid,
    val daysOfWeek: List<DayOfWeek>,
    val startTime: LocalTime,
    val endTime: LocalTime,
)

@Serializable
data class OneTimeAvailabilityRule(
    val id: Uuid,
    val serviceId: Uuid,
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)

@Serializable
data class AvailabilityBlock(
    val id: Uuid,
    val serviceId: Uuid,
    val from: LocalDateTime,
    val to: LocalDateTime,
    val reason: String,
)

data class BookingDomain(
    val id: Uuid,
    val userId: Uuid,
    val serviceId: Uuid,
    val startsAt: LocalDateTime,
    val endsAt: LocalDateTime,
    val status: BookingStatus = BookingStatus.ACTIVE,
    val cancelReason: String? = null,
)

enum class BookingStatus {
    ACTIVE,
    CANCELLED_BY_USER,
    CANCELLED_BY_ADMIN,
    CANCELLED_BY_SYSTEM,
    COMPLETED
}