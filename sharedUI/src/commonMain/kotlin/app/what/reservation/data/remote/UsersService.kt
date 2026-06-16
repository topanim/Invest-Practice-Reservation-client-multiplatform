package app.what.reservation.data.remote

import app.what.reservation.data.local.AuthSessionManager
import app.what.reservation.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.uuid.Uuid

class UsersService(
    private val client: HttpClient,
    private val authSessionManager: AuthSessionManager
) : ApiService(authSessionManager) {
    suspend fun me() = authCall {
        client.get("$baseUrl/users/me") {
            bearerAuth(authSessionManager.getAccessToken())
        }
    }.body<UserDto>()

    suspend fun clients() = authCall {
        client.get("$baseUrl/users/clients") {
            bearerAuth(authSessionManager.getAccessToken())
        }
    }.body<List<UserDto>>()

    suspend fun byId(userId: Uuid) = authCall {
        client.get("$baseUrl/users/$userId") {
            bearerAuth(authSessionManager.getAccessToken())
        }
    }.body<UserDto>()

    suspend fun search(query: String) = authCall {
        client.get("$baseUrl/users") {
            bearerAuth(authSessionManager.getAccessToken())
            parameter("q", query)
        }
    }.body<List<UserDto>>()
}