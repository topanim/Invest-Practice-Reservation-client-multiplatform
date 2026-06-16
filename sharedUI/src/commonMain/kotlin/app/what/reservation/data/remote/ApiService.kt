package app.what.reservation.data.remote

import app.what.reservation.data.local.AuthSessionManager
import io.ktor.client.statement.HttpResponse

abstract class ApiService(private val apiSessionManager: AuthSessionManager) {
    companion object {
        val baseUrl: String = "https://dotingly-together-cormorant.cloudpub.ru"
    }

    protected suspend fun authCall(block: suspend () -> HttpResponse): HttpResponse {
        val response = block()

        if (response.status.value == 401) {
            apiSessionManager.refresh()
            return block()
        }

        return response
    }
}