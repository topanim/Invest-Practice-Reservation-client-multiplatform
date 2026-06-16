package app.what.reservation.data.remote

import app.what.reservation.data.local.AuthSessionManager
import app.what.reservation.data.remote.dto.BookingService
import app.what.reservation.data.remote.dto.CreateServiceRequest
import app.what.reservation.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.uuid.Uuid

class ServicesService(
    private val client: HttpClient,
    private val apiSessionManager: AuthSessionManager
) : ApiService(apiSessionManager) {
    suspend fun my() = authCall {
        client.get("$baseUrl/services/my") {
            bearerAuth(apiSessionManager.getAccessToken())
        }
    }.body<List<BookingService>>()

    suspend fun getAll() = authCall {
        client.get("$baseUrl/services/all") {
            bearerAuth(apiSessionManager.getAccessToken())
        }
    }.body<List<BookingService>>()

    suspend fun get(id: Uuid) = authCall {
        client.get("$baseUrl/services/") {
            bearerAuth(apiSessionManager.getAccessToken())
            parameter("id", id)
        }
    }.body<BookingService>()

    suspend fun create(data: CreateServiceRequest) = authCall {
        client.post("$baseUrl/services") {
            bearerAuth(apiSessionManager.getAccessToken())
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }.body<BookingService>()

    suspend fun deactivate(id: Uuid) {
        authCall {
            client.delete("$baseUrl/services/deactivate") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("id", id)
            }
        }
    }

    suspend fun delete(id: Uuid) {
        authCall {
            client.delete("$baseUrl/services") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("id", id)
            }
        }
    }

    suspend fun getAdmins(serviceId: Uuid) = authCall {
        client.get("$baseUrl/services/admins") {
            bearerAuth(apiSessionManager.getAccessToken())
            parameter("id", serviceId)
        }
    }.body<List<UserDto>>()

    suspend fun addAdmin(serviceId: Uuid, adminId: Uuid) {
        authCall {
            client.post("$baseUrl/services/admins") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("id", serviceId)
                parameter("userId", adminId)
            }
        }
    }

    suspend fun addAdmin(serviceId: Uuid, adminIds: List<Uuid>) {
        authCall {
            client.post("$baseUrl/services/admins/many") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("id", serviceId)
                adminIds.forEach {
                    parameter("userId", it.toString())
                }
            }
        }
    }

    suspend fun removeAdmin(serviceId: Uuid, adminId: Uuid) {
        authCall {
            client.delete("$baseUrl/services/admins") {
                bearerAuth(apiSessionManager.getAccessToken())
                parameter("id", serviceId)
                parameter("userId", adminId)
            }
        }
    }
}