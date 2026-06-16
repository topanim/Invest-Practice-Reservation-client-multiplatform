package app.what.reservation.data.remote.dto

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class BookingService(
    val id: Uuid,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val price: Float,
    val bufferMinutes: Int,
    val isActive: Boolean
)

@Serializable
data class CreateServiceRequest(
    val name: String,
    val description: String,
    val durationMinutes: Int,
    val price: Float,
    val bufferMinutes: Int,
)

@Serializable
data class CreateAvailabilityRuleRequest(
    val daysOfWeek: List<DayOfWeek>,
    val startTime: LocalTime,
    val endTime: LocalTime
)

@Serializable
data class CreateOneTimeAvailabilityRuleRequest(
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)

@Serializable
data class CreateAvailabilityBlockRequest(
    val from: LocalDateTime,
    val to: LocalDateTime,
    val reason: String
)