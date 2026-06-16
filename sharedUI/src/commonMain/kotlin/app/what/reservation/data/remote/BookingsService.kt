package app.what.reservation.data.remote

import app.what.reservation.data.local.AuthSessionManager
import app.what.reservation.data.remote.dto.BookingDto
import app.what.reservation.data.remote.dto.BookingSlot
import app.what.reservation.data.remote.dto.CreateBookingRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

class BookingsService(
    private val client: HttpClient,
    private val authSessionManager: AuthSessionManager
) : ApiService(authSessionManager) {
    suspend fun allByService(serviceId: Uuid) = authCall {
        client.get("$baseUrl/bookings") {
            bearerAuth(authSessionManager.getAccessToken())
            parameter("serviceId", serviceId)
        }
    }.body<List<BookingDto>>()

    suspend fun my(serviceId: Uuid? = null) = authCall {
        client.get("$baseUrl/bookings/my") {
            bearerAuth(authSessionManager.getAccessToken())
            if (serviceId != null) parameter("serviceId", serviceId)
        }
    }.body<List<BookingDto>>()

    suspend fun cancel(id: Uuid, reason: String? = null) {
        authCall {
            client.patch("$baseUrl/bookings/cancel") {
                bearerAuth(authSessionManager.getAccessToken())
                parameter("id", id)
                if (reason != null) parameter("reason", reason)
            }
        }
    }

    suspend fun slots(serviceId: Uuid, date: LocalDate) = authCall {
        client.get("$baseUrl/bookings/slots") {
            bearerAuth(authSessionManager.getAccessToken())
            parameter("serviceId", serviceId)
            parameter("date", date)
        }
    }.body<List<BookingSlot>>()

    suspend fun create(data: CreateBookingRequest) = authCall {
        client.post("$baseUrl/bookings") {
            bearerAuth(authSessionManager.getAccessToken())
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }.body<BookingDto>()

    suspend fun delete(id: Uuid) {
        authCall {
            client.delete("$baseUrl/bookings") {
                bearerAuth(authSessionManager.getAccessToken())
                parameter("id", id)
            }
        }
    }
}