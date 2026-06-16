package app.what.reservation.data.local

import app.what.reservation.core.foundation.utils.orThrow
import app.what.reservation.data.remote.AuthService
import app.what.reservation.data.remote.dto.AuthCredentials
import app.what.reservation.utils.AppDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

class AuthSessionManager(
    private val authApi: AuthService,
    private val authStorage: AuthStorage,
) {
    private val scope: CoroutineScope = CoroutineScope(AppDispatchers.IO)
    private val mutex = Mutex()

    @Volatile
    private var refreshJob: Deferred<AuthCredentials>? = null

    fun setCredentials(credentials: AuthCredentials) {
        authStorage.userId.set(credentials.refreshToken.userId)
        authStorage.accessToken.set(credentials.accessToken)
        authStorage.refreshToken.set(credentials.refreshToken.id)
    }

    fun clearCredentials() {
        authStorage.userId.set(null)
        authStorage.accessToken.set(null)
        authStorage.refreshToken.set(null)
    }

    fun getAccessToken() = authStorage.accessToken.get()
        .orThrow { "access token wasn't set" }

    fun getAccessTokenOrNull() = authStorage.accessToken.get()

    suspend fun refresh(): AuthCredentials {
        refreshJob?.let { return it.await() }

        return mutex.withLock {
            refreshJob?.let { return@withLock it.await() }

            val job = scope.async {
                val refreshToken = authStorage.refreshToken.get()
                    ?: error("Refresh token not found")

                val credentials = authApi.refresh(refreshToken)

                authStorage.accessToken.set(credentials.accessToken)
                authStorage.refreshToken.set(credentials.refreshToken.id)

                credentials
            }

            refreshJob = job

            try {
                job.await()
            } finally {
                refreshJob = null
            }
        }
    }
}