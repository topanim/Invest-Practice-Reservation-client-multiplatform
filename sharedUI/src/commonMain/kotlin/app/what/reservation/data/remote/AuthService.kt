package app.what.reservation.data.remote

import app.what.reservation.data.remote.ApiService.Companion.baseUrl
import app.what.reservation.data.remote.dto.AuthCredentials
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import kotlin.uuid.Uuid

class AuthService(
    private val client: HttpClient,
) {
    suspend fun getGoogleAuthUrl(callbackUrl: String) =
        client.get("$baseUrl/auth/google") {
            parameter("redirection", callbackUrl)
        }.headers[HttpHeaders.Location]

    suspend fun refresh(refreshToken: Uuid) =
        client.post("$baseUrl/auth/refresh") {
            parameter("refreshToken", refreshToken)
        }.body<AuthCredentials>()

    suspend fun exchange(sessionId: Uuid) =
        client.post("$baseUrl/auth/exchange") {
            parameter("sessionId", sessionId)
        }.body<AuthCredentials>()
}